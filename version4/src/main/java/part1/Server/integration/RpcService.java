package part1.Server.integration;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {


    /**
     * 组号
     * @return
     */
    String group() default "1";

    /**
     * 版本号
     * @return
     */
    String version() default "1.0";

    /**
     * 是否可以重试
     * @return
     */
    boolean canRetry() default false;

    /**
     * 负载均衡策略
     */
    String loadBalance() default "RoundLoadBalance";

    /**
     * 是否开启服务
     * @return
     */
    boolean useAble() default true;

    /**
     * 限流阈值-桶令牌算法
     */
    int bucketCAPACITY() default 10;
}
