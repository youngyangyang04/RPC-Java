package com.kama.client.rpcclient;


import common.message.RpcRequest;
import common.message.RpcResponse;

/**
 * @InterfaceName RpcClient
 * @Description 定义底层通信方法
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:11
 * @Version v5.0
 */

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request);
    void close();
}
