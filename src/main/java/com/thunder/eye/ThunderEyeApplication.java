package com.thunder.eye;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Slf4j
public class ThunderEyeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThunderEyeApplication.class, args);
    }

}
