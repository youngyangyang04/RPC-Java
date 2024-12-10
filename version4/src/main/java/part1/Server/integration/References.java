package part1.Server.integration;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface References {

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
     * 负载均衡策略
     */
    String loadBalance() default "RoundLoadBalance";
}
