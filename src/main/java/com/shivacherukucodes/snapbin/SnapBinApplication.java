package com.shivacherukucodes.snapbin;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.shivacherukucodes.snapbin.repository")
public class SnapBinApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SnapBinApplication.class, args);
            LocalDateTime now = LocalDateTime.now();
            log.info("<<<<<<<<<<<< SnapBinApplication started at "+ now + " successfully >>>>>>>>>>>>");
            if (args.length > 0) {
                log.info("Application started with arguments:");
                for (String arg : args) {
                    log.info("ARG: {}", arg);
                }
            }
        } catch (Exception e) {
            log.error("Application failed to start: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void onShutdown() {
        log.info("<<<<<<<<<<<< SnapBinApplication is shutting down >>>>>>>>>>>>");
    }
}
