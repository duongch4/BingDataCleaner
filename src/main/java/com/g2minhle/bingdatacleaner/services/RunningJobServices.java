package com.g2minhle.bingdatacleaner.services;

import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.JobServices.UserAction;

public interface RunningJobServices {
	public void startJob(Job newJob);

	public void performActionOnJob(Job job, UserAction action)
			throws DatabaseConnectivityException;

	public void stopAndRemoveJob(String jobId);
}
