package com.cipher.ciphersystem;

import com.cipher.ciphersystem.property.FileProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.cipher.ciphersystem.mapper") //扫描的mapper
@SpringBootApplication
@EnableConfigurationProperties({
        FileProperties.class
})
public class CiphersystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiphersystemApplication.class, args);
    }

}
