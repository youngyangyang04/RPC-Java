package part1.Server.integration;

import org.springframework.context.annotation.Import;
import part1.Server.integration.configuration.ConsumerPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yty
 * @Description: 消费者项目启动注解
 * @DateTime: 2024/11/08 16:01
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ConsumerPostProcessor.class)
public @interface EnableConsumer {
}
