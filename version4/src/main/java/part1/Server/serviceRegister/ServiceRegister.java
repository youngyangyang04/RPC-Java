package part1.Server.serviceRegister;

import part1.Server.integration.RpcService;

import java.net.InetSocketAddress;

/**
 * @version 1.0
 * @create 2024/5/3 16:58
 */
// 服务注册接口
public interface ServiceRegister {
    //  注册：保存服务与地址。
    void register(String serviceName, InetSocketAddress serviceAddress,boolean canRetry);

    void register(String serviceName, InetSocketAddress serviceAddress, RpcService rpcService);

}
