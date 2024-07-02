package part1.Client.rpcClient;

import part1.common.Message.RpcResponse;
import part1.common.Message.RpcRequest;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/5/2 18:55
 */
public interface   RpcClient {

    //定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
