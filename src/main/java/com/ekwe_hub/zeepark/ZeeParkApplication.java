package com.ekwe_hub.zeepark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZeeParkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeeParkApplication.class, args);
    }

}
