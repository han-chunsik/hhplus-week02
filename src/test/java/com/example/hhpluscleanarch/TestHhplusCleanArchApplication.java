package com.example.hhpluscleanarch;

import org.springframework.boot.SpringApplication;

public class TestHhplusCleanArchApplication {

    public static void main(String[] args) {
        SpringApplication.from(HhplusCleanArchApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
