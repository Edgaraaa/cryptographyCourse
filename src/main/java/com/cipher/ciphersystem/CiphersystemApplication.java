package com.cipher.ciphersystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cipher.ciphersystem.mapper") //扫描的mapper
@SpringBootApplication
public class CiphersystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiphersystemApplication.class, args);
    }

}
