package com.grabtix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrabTixApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrabTixApplication.class, "--spring.profiles.active=prod");

    }
}
