package com.g2minhle.bingdatacleaner.services.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.EmailServices;

public class GmailServices implements EmailServices {
	public void notifyDoneJobDone(Job job) {
		try {
			String requestUrl =
					"https://script.google.com/macros/s/AKfycbyySwTuZPfEltoceGsdNSY-jyakmkmOFs1unJQZdcu1lDK0cLA/exec?recipient=%s&documentUrl=%s";
			String documentUrl =
					String.format(
							"https://docs.google.com/spreadsheets/d/%s",
							job.getDestinationDocumentId());
			requestUrl = requestUrl.format(requestUrl, job.getUserEmail(), documentUrl);
			Document doc =
					Jsoup.connect(requestUrl).timeout(10000).ignoreHttpErrors(true)
							.userAgent("Mozilla/5.0").post();
		} catch (Exception e) {
		}
	}
}
