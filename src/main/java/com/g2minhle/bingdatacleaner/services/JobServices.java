package com.g2minhle.bingdatacleaner.services;

import java.util.Map;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidActionNameException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.google.common.collect.ImmutableMap;

public interface JobServices {

	public enum UserAction {
		CANCEL, RESTART, PAUSE, RESUME
	}

	public static final Map<UserAction, Job.JobStatus> UserActionToJobStatus =
			ImmutableMap.of(
					UserAction.CANCEL, Job.JobStatus.CANCELLED,  
					UserAction.RESTART, Job.JobStatus.RUNNING, 
					UserAction.PAUSE, Job.JobStatus.PAUSED, 
					UserAction.RESUME, Job.JobStatus.RUNNING);

	public Job createJob(String userEmail, String documentUrl)
			throws CannotAccessToDocumentException, DatabaseConnectivityException,
			DocumentServiceConnectivityException, InvalidDocumentUrlException;

	public Job getJob(String jobId)
			throws JobNotFoundException, DatabaseConnectivityException;

	public void updateJob(String jobId, String userEmail)
			throws JobNotFoundException, DatabaseConnectivityException;

	public void performAnActionOnJob(String jobId, String action)
			throws JobNotFoundException, DatabaseConnectivityException,
			InvalidActionNameException;
}
