package part1.Client.serviceCenter;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part1.Client.cache.serviceCache;
import part1.Client.serviceCenter.ZkWatcher.watchZK;
import part1.Client.serviceCenter.balance.impl.ConsistencyHashBalance;
import part1.common.Message.RpcRequest;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ZKServiceCenter implements ServiceCenter{
    // curator 提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";
    //serviceCache
    private serviceCache cache;

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
        log.info("zookeeper 连接成功");
        //初始化本地缓存
        cache=new serviceCache();
        //加入zookeeper事件监听器
        watchZK watcher=new watchZK(client,cache);
        //监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }
    //根据服务名（接口名）返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先从本地缓存中找
            List<String> addressList=cache.getServiceListFromCache(serviceName);
            //如果找不到，再去zookeeper中找
            //这种i情况基本不会发生，或者说只会出现在初始化阶段
            if(addressList==null) {
                addressList=client.getChildren().forPath("/" + serviceName);
            }
            // 负载均衡得到地址（要先解析得到地址）
            String address = new ConsistencyHashBalance().balance(addressList);
            return parseAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析得到地址集合
     * @param addressList
     * @return
     */
    public List<String> getAddressList(List<String> addressList) {
        return addressList.stream().map(a -> a.split("-")[0]).collect(Collectors.toList());
    }


    // TODO 是否可重试的校验修改为 从zoo节点中的注解信息获取
    public boolean checkRetry(RpcRequest rpcRequest) {
        boolean canRetry =false;
        try {
            List<String> serviceList = client.getChildren()
                    .forPath("/" + rpcRequest.getInterfaceName() + "." + rpcRequest.getReferences().version());
//            for(String s:serviceList){
//                if(s.equals(serviceName)){
//                    log.info("服务"+serviceName+"在白名单上，可进行重试");
//                    canRetry=true;
//                }
//            }
            String serviceInfo = serviceList.get(0).split("-")[1];
            Map<String, String> infoMap = convertStringToHashMap(serviceInfo);
            return infoMap.get("canRetry").equals("true");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return canRetry;
    }
    public static Map<String, String> convertStringToHashMap(String str) {
        Map<String, String> map = new HashMap<>();
        if (str != null && !str.isEmpty()) {
            str = str.replace("{","").replace("}","");
            String[] pairs = str.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return map;
    }
    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }
    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split("-")[0].split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
