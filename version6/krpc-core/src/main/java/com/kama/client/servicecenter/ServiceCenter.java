package com.kama.client.servicecenter;


import common.message.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @InterfaceName ServiceCenter
 * @Description 服务中心接口
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:31
 * @Version v5.0
 */

public interface ServiceCenter {
    //  查询：根据服务名查找地址
    InetSocketAddress serviceDiscovery(RpcRequest request);

    //判断是否可重试
    boolean checkRetry(InetSocketAddress serviceAddress, String methodSignature);

    //关闭客户端
    void close();
}
