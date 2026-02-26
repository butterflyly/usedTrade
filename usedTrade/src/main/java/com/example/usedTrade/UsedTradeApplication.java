package com.example.usedTrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.usedTrade.Entity")

public class UsedTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsedTradeApplication.class, args);
	}

}