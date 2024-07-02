package part1.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/7/3 0:22
 */
public class CircuitBreakerProvider {
    private Map<String,CircuitBreaker> circuitBreakerMap=new HashMap<>();

    public CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker=circuitBreakerMap.get(serviceName);
        }else {
            circuitBreaker=new CircuitBreaker(3,0.5,10000);
        }
        return circuitBreaker;
    }
}
