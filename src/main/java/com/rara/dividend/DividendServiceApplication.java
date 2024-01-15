package com.rara.dividend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DividendServiceApplication.class, args);
	}

}
