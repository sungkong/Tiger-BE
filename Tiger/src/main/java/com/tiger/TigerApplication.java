package com.tiger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class TigerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TigerApplication.class, args);
    }

}
