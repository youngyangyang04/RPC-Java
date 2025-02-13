package com.kama.client.servicecenter;

import com.kama.client.cache.ServiceCache;
import com.kama.client.servicecenter.ZKWatcher.watchZK;
import com.kama.client.servicecenter.balance.LoadBalance;
import com.kama.client.servicecenter.balance.impl.ConsistencyHashBalance;
import common.message.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName ZKServiceCenter
 * @Description 从服务中心获取服务地址
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:33
 * @Version v5.0
 */
@Slf4j
public class ZKServiceCenter implements ServiceCenter {
    // curator 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";
    //serviceCache
    private ServiceCache cache;

    private final LoadBalance loadBalance = new ConsistencyHashBalance();

    //负责zookeeper客户端的初始化，并与zookeeper服务端进行连接
    public ZKServiceCenter() throws InterruptedException {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        log.info("Zookeeper 连接成功");
        //初始化本地缓存
        cache = new ServiceCache();
        //加入zookeeper事件监听器
        watchZK watcher = new watchZK(client, cache);
        //监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }

    //根据服务名（接口名）返回地址
    @Override
    public InetSocketAddress serviceDiscovery(RpcRequest request) {
        String serviceName = request.getInterfaceName();
        try {
            //先从本地缓存中找
            List<String> addressList = cache.getServiceListFromCache(serviceName);
            //如果找不到，再去zookeeper中找
            //这种i情况基本不会发生，或者说只会出现在初始化阶段
            if (addressList == null) {
                addressList = client.getChildren().forPath("/" + serviceName);
                // 如果本地缓存中没有该服务名的地址列表，则添加
                List<String> cachedAddresses = cache.getServiceListFromCache(serviceName);
                if (cachedAddresses == null || cachedAddresses.isEmpty()) {
                    // 假设 addServiceToCache 方法可以处理单个地址
                    for (String address : addressList) {
                        cache.addServiceToCache(serviceName, address);
                    }
                }
            }
            if (addressList.isEmpty()) {
                log.warn("未找到服务：{}", serviceName);
                return null;
            }
            // 负载均衡得到地址
            String address = loadBalance.balance(addressList);
            return parseAddress(address);
        } catch (Exception e) {
            log.error("服务发现失败，服务名：{}", serviceName, e);
        }
        return null;
    }
    //保证线程安全使用CopyOnWriteArraySet
    private Set<String> retryServiceCache = new CopyOnWriteArraySet<>();
    //写一个白名单缓存，优化性能
    @Override
    public boolean checkRetry(InetSocketAddress serviceAddress, String methodSignature) {
        if (retryServiceCache.isEmpty()) {
            try {
                CuratorFramework rootClient = client.usingNamespace(RETRY);
                List<String> retryableMethods = rootClient.getChildren().forPath("/" + getServiceAddress(serviceAddress));
                retryServiceCache.addAll(retryableMethods);
            } catch (Exception e) {
                log.error("检查重试失败，方法签名：{}", methodSignature, e);
            }
        }
        return retryServiceCache.contains(methodSignature);
    }

    @Override
    public void close() {
        client.close();
    }

    // 将InetSocketAddress解析为格式为ip:port的字符串
    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
