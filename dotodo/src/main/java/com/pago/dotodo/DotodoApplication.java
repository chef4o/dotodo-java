package com.pago.dotodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DotodoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DotodoApplication.class, args);
    }

}
