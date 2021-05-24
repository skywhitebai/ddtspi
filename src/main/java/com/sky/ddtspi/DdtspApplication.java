package com.sky.ddtspi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.sky.ddtspi.dao.custom")
public class DdtspApplication {
	public static void main(String[] args) {
		SpringApplication.run(DdtspApplication.class, args);
	}

}
