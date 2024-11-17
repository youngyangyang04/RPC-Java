package part1.Server.integration;


import org.springframework.context.annotation.Import;
import part1.Server.integration.configuration.ProviderPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProviderPostProcessor.class)
public @interface EnableProvider {

}
