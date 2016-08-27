package com.g2minhle.bingdatacleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BingDataCleanerApplication {

	public static final String APPLICATION_NAME = "BingDataCleaner";
	
	public static void main(String[] args) {
		SpringApplication.run(BingDataCleanerApplication.class, args);
	}
}
