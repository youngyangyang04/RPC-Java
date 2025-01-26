package com.kama.server.serviceRegister;


import java.net.InetSocketAddress;

/**
 * @InterfaceName ServiceRegister
 * @Description 服务注册接口
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:27
 * @Version v5.0
 */

public interface ServiceRegister {
    void register(Class<?> clazz, InetSocketAddress serviceAddress);
}
