package com.g2minhle.bingdatacleaner.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidActionNameException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.model.Job.JobStatus;
import com.g2minhle.bingdatacleaner.services.DatabaseServices;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.g2minhle.bingdatacleaner.services.JobServices;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;

public class BasicJobServices implements JobServices {

	@Autowired
	DocumentServices documentServices;

	@Autowired
	DatabaseServices databaseServices;

	@Autowired
	RunningJobServices runningJobServices;

	@Override
	public Job createJob(String userEmail, String documentUrl)
			throws CannotAccessToDocumentException, DatabaseConnectivityException,
			DocumentServiceConnectivityException, InvalidDocumentUrlException {
		String sourceDocumentId = documentServices.getDocumentIdFromUrl(documentUrl);

		// documentServices check accessibility by trying to read number of rows
		Long totalWork = documentServices.getDocumentSize(sourceDocumentId);
		// documentServices create new file and assign owner ship to given a
		// given user
		String destinationDocumentId =
				documentServices.createDestinationDocument(userEmail);

		Job newJob =
				new Job.JobbBuilder()
						.withId(destinationDocumentId)
						.withSourceDocumentId(sourceDocumentId)
						.withDestincationDocumentId(destinationDocumentId)
						.withUserEmail(userEmail)
						.withStatus(JobStatus.CREATED)
						.withTotalWork(totalWork)
						.withProgress(0L)
						.withCreatedTime(new Date())
						.build();
		databaseServices.saveJob(newJob);
		runningJobServices.startJob(newJob);
		return newJob;
	}

	@Override
	public Job getJob(String jobId)
			throws DatabaseConnectivityException, JobNotFoundException {
		return databaseServices.getJob(jobId);
	}

	@Override
	public void updateJob(String jobId, String userEmail)
			throws JobNotFoundException, DatabaseConnectivityException {
		Job job = this.getJob(jobId);
		// TODO change to real patch not save full job to save data + compute
		// power
		Job newJob = new Job.JobbBuilder().withJob(job).withUserEmail(userEmail).build();
		databaseServices.saveJob(newJob);
	}

	@Override
	public void performAnActionOnJob(String jobId, String action)
			throws JobNotFoundException, DatabaseConnectivityException,
			InvalidActionNameException {
		// TODO Auto-generated method stub
		Job job = this.getJob(jobId);
		UserAction userAction;
		try {
			userAction = UserAction.valueOf(action);
		} catch (Exception e) {
			throw new InvalidActionNameException();
		}
		runningJobServices.performActionOnJob(job, userAction);
	}

}
