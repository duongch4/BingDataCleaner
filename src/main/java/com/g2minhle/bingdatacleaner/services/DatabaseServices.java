package com.g2minhle.bingdatacleaner.services;

import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.model.Job;

public interface DatabaseServices {
	
	public Job getJob(String jobId) throws DatabaseConnectivityException;

	public void saveJob(Job newJob) throws DatabaseConnectivityException;

}
