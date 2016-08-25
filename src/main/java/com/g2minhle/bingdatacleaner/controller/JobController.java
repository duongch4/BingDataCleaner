package com.g2minhle.bingdatacleaner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jobs")
public class JobController {

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createJob(
			@PathVariable("userEmail") String userEmail,
			@PathVariable("documentURL") String documentURL) {
		return "Create job for user " + userEmail + " with document url: " + documentURL;
	}

	@ResponseBody
	@RequestMapping(value = "/{jobID}", method = RequestMethod.GET)
	public String getJobInformation(String jobID) {
		return "This is information of job: " + jobID;
	}

	@ResponseBody
	@RequestMapping(value = "/{jobID}", method = RequestMethod.PATCH)
	public String updateJobInformation(
			@PathVariable("jobID") String jobID,
			String userEmail) {
		return "Updated user email for job " + jobID + " to " + userEmail;
	}

	@ResponseBody
	@RequestMapping(value = "/{jobID}/action", method = RequestMethod.POST)
	public String performAnActionOnJob(
			@PathVariable("jobID") String jobID,
			String action) {
		return "This is information of job: " + jobID;
	}
}
