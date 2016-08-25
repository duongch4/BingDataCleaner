package com.g2minhle.bingdatacleaner.services;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;

public interface JobServices {

	public Job createJob(String userEmail, String documentURL)
			throws CannotAccessToDocumentException, DatabaseConnectivityException,
			DocumentServiceConnectivityException;

	public Job getJob(String jobID)
			throws JobNotFoundException, DatabaseConnectivityException;

	public String updateJob(String jobID, String userEmail)
			throws JobNotFoundException, DatabaseConnectivityException;

	public String performAnActionOnJob(String jobID, String action)
			throws JobNotFoundException, DatabaseConnectivityException;
}
