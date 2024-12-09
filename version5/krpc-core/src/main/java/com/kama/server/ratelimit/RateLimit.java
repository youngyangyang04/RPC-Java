package com.kama.server.ratelimit;


/**
 * @InterfaceName RateLimit
 * @Description 限流接口
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:50
 * @Version v5.0
 */

public interface RateLimit {
    //获取访问许可
    boolean getToken();
}
