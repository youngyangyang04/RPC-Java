package part1.Client.circuitBreaker;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class CircuitBreakerProvider {
    private Map<String,CircuitBreaker> circuitBreakerMap=new HashMap<>();

    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker=circuitBreakerMap.get(serviceName);
        } else {
            log.info("服务:{}:创建新熔断器",serviceName);
            circuitBreaker = new CircuitBreaker(1,0.5,10000);
            circuitBreakerMap.put(serviceName,circuitBreaker);
        }
        return circuitBreaker;
    }
}
