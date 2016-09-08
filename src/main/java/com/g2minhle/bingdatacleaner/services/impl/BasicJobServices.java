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
	DocumentServices _documentServices;

	@Autowired
	DatabaseServices _databaseServices;

	@Autowired
	RunningJobServices _runningJobServices;
	
	private static BasicJobServices instance = null;

	private BasicJobServices() {
		// Exists only to defeat instantiation.
	}

	public static BasicJobServices getInstance() {
		if (instance == null) {
			instance = new BasicJobServices();
		}
		return instance;
	}

	@Override
	public Job createJob(String userEmail, String documentUrl)
			throws CannotAccessToDocumentException, DatabaseConnectivityException,
			DocumentServiceConnectivityException, InvalidDocumentUrlException {
		String sourceDocumentId = _documentServices.getDocumentIdFromUrl(documentUrl);

		Long totalWork = _documentServices.getDocumentSize(sourceDocumentId);
		// documentServices create new file and assign owner ship to given a
		// given user
		String destinationDocumentId =
				_documentServices.createDestinationDocument(userEmail);

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
		_databaseServices.saveJob(newJob);
		_runningJobServices.startJob(newJob);
		return newJob;
	}

	@Override
	public Job getJob(String jobId)
			throws DatabaseConnectivityException, JobNotFoundException {
		return _databaseServices.getJob(jobId);
	}

	@Override
	public void updateJob(String jobId, String userEmail)
			throws JobNotFoundException, DatabaseConnectivityException {
		Job job = this.getJob(jobId);
		Job newJob = new Job.JobbBuilder().withJob(job).withUserEmail(userEmail).build();
		_databaseServices.saveJob(newJob);
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
		_runningJobServices.performActionOnJob(job, userAction);
	}

}
