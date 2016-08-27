package com.g2minhle.bingdatacleaner.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidActionNameException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.JobServices;

@Controller
@RequestMapping(value = "/jobs")
public class JobController {

	@Autowired
	JobServices jobServices;

	private final static Logger LOGGER = Logger.getLogger(JobController.class.getName());

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Job createJob(
			String userEmail,
			String documentUrl,
			HttpServletResponse response) {
		try {
			LOGGER.debug(
					String.format(
							"Request to create new job for %s with with %s",
							userEmail,
							documentUrl));
			Job newJob = jobServices.createJob(userEmail, documentUrl);
			response.setStatus(HttpServletResponse.SC_CREATED);
			return newJob;
		} catch (CannotAccessToDocumentException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
		} catch (DatabaseConnectivityException e) {
			// TODO add warning
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (DocumentServiceConnectivityException e) {
			// TODO add warning
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (InvalidDocumentUrlException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			// TODO add alert to this issue
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
	public Job getJobInformation(
			@PathVariable String jobId,
			HttpServletResponse response) {
		try {
			return jobServices.getJob(jobId);
		} catch (JobNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseConnectivityException e) {
			// TODO add warning
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			// TODO add alert to this issue
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/{jobId}", method = RequestMethod.PATCH)
	public void updateJobInformation(
			@PathVariable("jobId") String jobId,
			String userEmail,
			HttpServletResponse response) {
		try {
			jobServices.updateJob(jobId, userEmail);
		} catch (JobNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseConnectivityException e) {
			// TODO add warning
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			// TODO add alert to this issue
		}
	}

	@ResponseBody
	@RequestMapping(value = "/{jobId}/action", method = RequestMethod.POST)
	public void performAnActionOnJob(
			@PathVariable("jobId") String jobId,
			String action,
			HttpServletResponse response) {
		try {
			jobServices.performAnActionOnJob(jobId, action);
		} catch (JobNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseConnectivityException e) {
			// TODO add warning
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (InvalidActionNameException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			// TODO add alert to this issue
		}
	}
}
