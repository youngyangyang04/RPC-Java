package part1.Server.ratelimit.provider;

import part1.Server.ratelimit.RateLimit;
import part1.Server.ratelimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @create 2024/7/2 1:45
 */
public class RateLimitProvider {
    private Map<String, RateLimit> rateLimitMap=new HashMap<>();

    public RateLimit getRateLimit(String interfaceName){
        if(!rateLimitMap.containsKey(interfaceName)){
            // 默认设定 令牌产生速率:100ms  容量:10
            RateLimit rateLimit = new TokenBucketRateLimitImpl(100,10);
            rateLimitMap.put(interfaceName,rateLimit);
            return rateLimit;
        }
        return rateLimitMap.get(interfaceName);
    }
}