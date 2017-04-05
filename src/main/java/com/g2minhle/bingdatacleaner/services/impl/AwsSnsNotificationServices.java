package com.g2minhle.bingdatacleaner.services.impl;

import com.g2minhle.bingdatacleaner.services.NotificationServices;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

public class AwsSnsNotificationServices implements NotificationServices {

	private static final String ALERT_ARN_ENV_VAR = "ALERT_ARN"; 
	
	private static final String WARNING_ARN_ENV_VAR = "WARNING_ARN";
	
	private static AwsSnsNotificationServices Instance = null;

	private static final String _alertArn;

	private static final String _warningArn;
	
	static {
		_alertArn = System.getenv(ALERT_ARN_ENV_VAR);
		_warningArn = System.getenv(WARNING_ARN_ENV_VAR);
	}

	// create a new SNS client and set end point
	private AmazonSNSClient snsClient = new AmazonSNSClient();

	private AwsSnsNotificationServices() {
		// Exists only to defeat instantiation.
	}

	public static AwsSnsNotificationServices getInstance() {
		if (Instance == null) {
			Instance = new AwsSnsNotificationServices();
		}
		return Instance;
	}

	public void alert(String message) {
		PublishRequest publishRequest = new PublishRequest(_alertArn, message);
		snsClient.publish(publishRequest);
	}

	public void warning(String message) {
		PublishRequest publishRequest = new PublishRequest(_warningArn, message);
		snsClient.publish(publishRequest);
	}
}
