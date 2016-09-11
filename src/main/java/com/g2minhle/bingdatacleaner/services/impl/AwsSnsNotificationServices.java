package com.g2minhle.bingdatacleaner.services.impl;

import com.g2minhle.bingdatacleaner.services.NotificationServices;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

public class AwsSnsNotificationServices implements NotificationServices {

	private static AwsSnsNotificationServices Instance = null;

	private static final String ALERT_ARN =
			"arn:aws:sns:us-east-1:062478782561:BingDataCleanerIssues";

	private static final String WARNING_ARN =
			"arn:aws:sns:us-east-1:062478782561:BingDataCleanerWarning";

	// create a new SNS client and set endpoint
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
		PublishRequest publishRequest = new PublishRequest(ALERT_ARN, message);
		snsClient.publish(publishRequest);
	}

	public void warning(String message) {
		PublishRequest publishRequest = new PublishRequest(WARNING_ARN, message);
		snsClient.publish(publishRequest);
	}
}
