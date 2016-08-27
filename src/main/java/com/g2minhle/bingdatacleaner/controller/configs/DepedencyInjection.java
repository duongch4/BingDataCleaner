package com.g2minhle.bingdatacleaner.controller.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.DatabaseServices;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.g2minhle.bingdatacleaner.services.JobServices;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;
import com.g2minhle.bingdatacleaner.services.JobServices.UserAction;
import com.g2minhle.bingdatacleaner.services.impl.BasicJobServices;
import com.g2minhle.bingdatacleaner.services.impl.BasicRunningJobServices;
import com.g2minhle.bingdatacleaner.services.impl.DynamoDBServices;
import com.g2minhle.bingdatacleaner.services.impl.GoogleSheetServices;

@Configuration
public class DepedencyInjection {

	public final static JobServices basicJobServices = new BasicJobServices();
	public final static DynamoDBServices dynamoDBServices = new DynamoDBServices();
	public final static DocumentServices documentServices = new GoogleSheetServices();
	public final static RunningJobServices runningJobServices = new BasicRunningJobServices();
	@Bean
	public JobServices jobServices() {
		return basicJobServices;
	}

	@Bean
	public DatabaseServices databaseServices() {
		return dynamoDBServices;
	}

	@Bean
	public DocumentServices documentServices() {
		return documentServices;
	}

	@Bean
	public RunningJobServices runningJobServices() {
		return runningJobServices;
	}

}
