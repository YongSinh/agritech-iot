package com.agritechiot.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AgritechIotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgritechIotApplication.class, args);
    }

}
