package com.app.creditanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CreditAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditAnalysisApplication.class, args);
	}

}
