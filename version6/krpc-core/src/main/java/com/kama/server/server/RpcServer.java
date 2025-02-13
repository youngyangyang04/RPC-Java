package com.kama.server.server;


/**
 * @InterfaceName RpcServer
 * @Description 服务端接口
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:21
 * @Version v1.0
 */

public interface RpcServer {
    void start(int port);

    void stop();
}
