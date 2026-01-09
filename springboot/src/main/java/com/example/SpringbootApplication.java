package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * TradeLoop Application - Main Entry Point
 * A full-stack second-hand trading platform
 */
@SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  TradeLoop Application Started");
        System.out.println("  Access: http://localhost:9090");
        System.out.println("========================================\n");
    }

}
