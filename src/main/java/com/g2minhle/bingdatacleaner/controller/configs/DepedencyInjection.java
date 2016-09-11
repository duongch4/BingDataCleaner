package com.g2minhle.bingdatacleaner.controller.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.g2minhle.bingdatacleaner.services.DatabaseServices;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.g2minhle.bingdatacleaner.services.JobServices;
import com.g2minhle.bingdatacleaner.services.NotificationServices;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;
import com.g2minhle.bingdatacleaner.services.impl.AwsSnsNotificationServices;
import com.g2minhle.bingdatacleaner.services.impl.BasicJobServices;
import com.g2minhle.bingdatacleaner.services.impl.BasicRunningJobServices;
import com.g2minhle.bingdatacleaner.services.impl.DynamoDBServices;
import com.g2minhle.bingdatacleaner.services.impl.GoogleSheetServices;

@Configuration
public class DepedencyInjection {

	public final static JobServices _basicJobServices = BasicJobServices.getInstance();
	public final static DynamoDBServices _dynamoDBServices = DynamoDBServices.getInstance();
	public final static DocumentServices _documentServices = GoogleSheetServices.getInstance();
	public final static RunningJobServices _runningJobServices = BasicRunningJobServices.getInstance();
	public final static NotificationServices _notificationServices = AwsSnsNotificationServices.getInstance();
	
	@Bean
	public JobServices jobServices() {
		return _basicJobServices;
	}

	@Bean
	public DatabaseServices databaseServices() {
		return _dynamoDBServices;
	}

	@Bean
	public DocumentServices documentServices() {
		return _documentServices;
	}

	@Bean
	public RunningJobServices runningJobServices() {
		return _runningJobServices;
	}
	
	@Bean
	public NotificationServices notificationServices() {
		return _notificationServices;
	}

}
