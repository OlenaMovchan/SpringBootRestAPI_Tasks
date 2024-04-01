package com.example.tasknewspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
public class TaskNewSpringApplication {

    public static void main(String[] args) {

        SpringApplication.run(TaskNewSpringApplication.class, args);
    }

}
