package com.easy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableCaching  // enable spring-cache
public class EasyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyApplication.class, args);
        log.info("server started");
    }
}
