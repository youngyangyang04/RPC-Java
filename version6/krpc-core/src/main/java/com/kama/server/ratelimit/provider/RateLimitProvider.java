package com.kama.server.ratelimit.provider;

import com.kama.server.ratelimit.RateLimit;
import com.kama.server.ratelimit.impl.TokenBucketRateLimitImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RateLimitProvider
 * @Description 提供限流器
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:54
 * @Version v5.0
 */
@Slf4j
public class RateLimitProvider {
    private final Map<String, RateLimit> rateLimitMap = new ConcurrentHashMap<>();

    // 默认的限流桶容量和令牌生成速率
    private static final int DEFAULT_CAPACITY = 10;
    private static final int DEFAULT_RATE = 100;

    // 提供限流实例
    public RateLimit getRateLimit(String interfaceName) {
        return rateLimitMap.computeIfAbsent(interfaceName, key -> {
            RateLimit rateLimit = new TokenBucketRateLimitImpl(DEFAULT_RATE, DEFAULT_CAPACITY);
            log.info("为接口 [{}] 创建了新的限流策略: {}", interfaceName, rateLimit);
            return rateLimit;
        });
    }
}
