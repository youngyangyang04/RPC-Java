package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import part1.Server.integration.EnableConsumer;

@SpringBootApplication
@EnableConsumer
//@ComponentScan(basePackages = {"com.example.consumer.web.controller"})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
