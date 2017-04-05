package com.g2minhle.bingdatacleaner.services.impl;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.EmailServices;

public class GmailServices implements EmailServices {
	
	private static final String NOTIFICATION_SCRIPT_URL_ENV_VAR = "NOTIFICATION_SCRIPT_URL";
	
	private String _notificationScriptUrl;
	
	public GmailServices(){
		_notificationScriptUrl = System.getenv(NOTIFICATION_SCRIPT_URL_ENV_VAR);
	}
	
	public void notifyDoneJobDone(Job job) throws IOException {
		String documentUrl = String.format(
				"https://docs.google.com/spreadsheets/d/%s", 
				job.getDestinationDocumentId());
		String notificationScriptUrl = String.format(
				_notificationScriptUrl, 
				job.getUserEmail(), 
				documentUrl);
		Jsoup.connect(notificationScriptUrl)
			 .timeout(10000)
			 .ignoreHttpErrors(true)
			 .userAgent("Mozilla/5.0").post();
	}
}
