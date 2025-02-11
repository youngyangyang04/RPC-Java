package com.kama.provider;

import com.kama.KRpcApplication;
import com.kama.provider.impl.UserServiceImpl;
import com.kama.server.provider.ServiceProvider;
import com.kama.server.server.RpcServer;
import com.kama.server.server.impl.NettyRpcServer;
import com.kama.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ProviderExample
 * @Description 测试服务端
 * @Author Tong
 * @LastChangeDate 2024-12-05 0:34
 * @Version v5.0
 */
@Slf4j
public class ProviderTest {

    public static void main(String[] args) throws InterruptedException {
        KRpcApplication.initialize();
        String ip=KRpcApplication.getRpcConfig().getHost();
        int port=KRpcApplication.getRpcConfig().getPort();
        // 创建 UserService 实例
        UserService userService = new UserServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider(ip, port);
        // 发布服务接口到 ServiceProvider
        serviceProvider.provideServiceInterface(userService);  // 可以设置是否支持重试

        // 启动 RPC 服务器并监听端口
        RpcServer rpcServer = new NettyRpcServer(serviceProvider);
        rpcServer.start(port);  // 启动 Netty RPC 服务，监听 port 端口
        log.info("RPC 服务端启动，监听端口" + port);
    }

}
