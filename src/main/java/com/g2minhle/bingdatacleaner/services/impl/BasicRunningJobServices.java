package com.g2minhle.bingdatacleaner.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.model.Job.JobStatus;
import com.g2minhle.bingdatacleaner.services.JobServices.UserAction;
import com.g2minhle.bingdatacleaner.services.DatabaseServices;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;
import com.g2minhle.bingdatacleaner.worker.BingDataCleanerWorker;

public class BasicRunningJobServices implements RunningJobServices {

	@Autowired
	DatabaseServices _databaseServices;

	private static BasicRunningJobServices instance = null;

	private BasicRunningJobServices() {
		// Exists only to defeat instantiation.
	}
	
	public static BasicRunningJobServices getInstance() {
		if (instance == null) {
			instance = new BasicRunningJobServices();
		}
		return instance;
	}

	Map<String, BingDataCleanerWorker> _threads =
			new HashMap<String, BingDataCleanerWorker>();

	@Override
	public synchronized void startJob(Job newJob) {
		BingDataCleanerWorker newWorker = new BingDataCleanerWorker(newJob, this);
		_threads.put(newJob.getId(), newWorker);
		newWorker.start();
	}

	@Override
	public synchronized void performActionOnJob(Job job, UserAction action)
			throws DatabaseConnectivityException {
		stopAndRemoveJob(job.getId());
		Job.JobbBuilder newJobBuilder = new Job.JobbBuilder().withJob(job);
		switch (action) {
		case PAUSE:
			newJobBuilder.withStatus(JobStatus.PAUSED);
			break;
		case CANCEL:
			newJobBuilder.withStatus(JobStatus.CANCELLED);
			break;
		case RESTART:
			newJobBuilder.withProgress(0L);
		case RESUME:
			startJob(newJobBuilder.build());
			break;
		}
		_databaseServices.saveJob(newJobBuilder.build());
	}

	public synchronized void stopAndRemoveJob(String jobId) {
		BingDataCleanerWorker workerThread = _threads.get(jobId);
		if (workerThread != null) {
			workerThread.setKeepRunning(false);
		}
		_threads.remove(jobId);
	}

}
