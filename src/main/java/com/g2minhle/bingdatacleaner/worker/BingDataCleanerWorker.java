package com.g2minhle.bingdatacleaner.worker;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.model.Job.JobStatus;
import com.g2minhle.bingdatacleaner.services.DatabaseServices;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.g2minhle.bingdatacleaner.services.EmailServices;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;
import com.g2minhle.bingdatacleaner.services.impl.DynamoDBServices;
import com.g2minhle.bingdatacleaner.services.impl.GmailServices;
import com.g2minhle.bingdatacleaner.services.impl.GoogleSheetServices;

public class BingDataCleanerWorker extends Thread {

	DocumentServices _documentServices = GoogleSheetServices.getInstance();

	DatabaseServices _databaseServices = DynamoDBServices.getInstance();

	RunningJobServices _runningJobServices;
	
	EmailServices _emailServices = new GmailServices();

	Job _job;
	Boolean _keepRunning;

	public void setKeepRunning(Boolean keepRunning) {
		_keepRunning = keepRunning;
	}

	public Job getJob() {
		return _job;
	}

	public BingDataCleanerWorker(Job job, RunningJobServices runningJobServices) {
		_job = job;
		_keepRunning = true;
		_runningJobServices = runningJobServices;
	}

	String searchBing(String searchTerm) {
		try {
			Document doc =
					Jsoup.connect("https://www.bing.com/search?q=" + searchTerm)
							.timeout(10000).ignoreHttpErrors(true)
							.userAgent("Mozilla/5.0").get();

			Elements resultsLink = doc.select(".b_algo h2 a");
			String firstUrlBing = resultsLink.get(0).attr("href");
			return firstUrlBing;
		} catch (Exception e) {
			return null;
		}
	}

	public void run() {
		try {
			_job =
					new Job.JobbBuilder().withJob(_job).withStatus(JobStatus.RUNNING)
							.build();
			List<String> entries;
			List<String> searchResults = new LinkedList<String>();

			Long progress = _job.getProgress();
			Long onePercent = _job.getTotalWork() / 100 + 1;
			Long itemPerBatch = Long.min(20L, onePercent);

			for (progress = _job.getProgress(); progress < _job.getTotalWork();) {
				if (!_keepRunning)
					return;
				itemPerBatch = Long.min(itemPerBatch, _job.getTotalWork() - progress);
				searchResults = new LinkedList<String>();
				entries =
						_documentServices.readFromDocument(
								_job.getSourceDocumentId(),
								progress,
								itemPerBatch);
				for (int i = 0; i < entries.size(); i++) {
					if (!_keepRunning)
						return;
					searchResults.add(searchBing(entries.get(i)));
				}

				_documentServices.writeToDocument(
						_job.getDestinationDocumentId(),
						progress,
						entries,
						searchResults);
				progress += entries.size();
				_job = new Job.JobbBuilder().withJob(_job).withProgress(progress).build();
				_databaseServices.saveJob(_job);

			}
			_job =
					new Job.JobbBuilder().withJob(_job).withStatus(JobStatus.DONE)
							.withDoneTime(new Date()).build();
			_databaseServices.saveJob(_job);
			_runningJobServices.stopAndRemoveJob(_job.getId());
			_emailServices.notifyDoneJobDone(_job);
		} catch (Exception e) {

		}
	}
}
