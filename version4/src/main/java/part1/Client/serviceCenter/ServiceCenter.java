package part1.Client.serviceCenter;

import part1.common.Message.RpcRequest;

import java.net.InetSocketAddress;


//服务中心接口
public interface ServiceCenter {
    //  查询：根据服务名查找地址
    InetSocketAddress serviceDiscovery(String serviceName);
    //判断是否可重试
    boolean checkRetry(RpcRequest rpcRequest) ;
}
