package com.kama.server.serviceRegister.impl;

import com.kama.annotation.Retryable;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kama.server.serviceRegister.ServiceRegister;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ZKServiceRegister
 * @Description zk服务注册中心
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:28
 * @Version v5.0
 */
@Slf4j
public class ZKServiceRegister implements ServiceRegister {
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";

    public ZKServiceRegister() {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        log.info("Zookeeper 连接成功");
    }

    @Override
    public void register(Class<?> clazz, InetSocketAddress serviceAddress) {
        String serviceName = clazz.getName();
        try {
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
                log.info("服务节点 {} 创建成功", "/" + serviceName);
            }

            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            if (client.checkExists().forPath(path) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                log.info("服务地址 {} 注册成功", path);
            } else {
                log.info("服务地址 {} 已经存在，跳过注册", path);
            }

            // 注册白名单
            List<String> retryableMethods = getRetryableMethod(clazz);
            log.info("可重试的方法: {}", retryableMethods);
            CuratorFramework rootClient = client.usingNamespace(RETRY);
            for (String retryableMethod : retryableMethods) {
                rootClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/" + getServiceAddress(serviceAddress) + "/" + retryableMethod);
            }
        } catch (Exception e) {
            log.error("服务注册失败，服务名：{}，错误信息：{}", serviceName, e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "zookeeper";
    }

    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    // 判断一个方法是否加了Retryable注解
    private List<String> getRetryableMethod(Class<?> clazz){
        List<String> retryableMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Retryable.class)) {
                String methodSignature = getMethodSignature(clazz, method);
                retryableMethods.add(methodSignature);
            }
        }
        return retryableMethods;
    }

    private String getMethodSignature(Class<?> clazz, Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName()).append("#").append(method.getName()).append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            sb.append(parameterTypes[i].getName());
            if (i < parameterTypes.length - 1) {
                sb.append(",");
            } else{
                sb.append(")");
            }
        }
        return sb.toString();
    }
}
