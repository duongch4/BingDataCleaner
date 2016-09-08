package com.g2minhle.bingdatacleaner.services;

import java.util.List;

import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;

public interface DatabaseServices {

	public Job getJob(String jobId)
			throws DatabaseConnectivityException, JobNotFoundException;

	public void saveJob(Job newJob) throws DatabaseConnectivityException;

	public List<Job> getJobs() throws DatabaseConnectivityException;

}
