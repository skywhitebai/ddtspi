package com.sky.ddtsp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sky.ddtsp.dao.custom")
public class DdtspApplication {
	public static void main(String[] args) {
		SpringApplication.run(DdtspApplication.class, args);
	}

}
