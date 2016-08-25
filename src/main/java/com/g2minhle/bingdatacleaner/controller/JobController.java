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
	@RequestMapping(value = "/{jobID}/", method = RequestMethod.GET)
	public String getJobInformation(@PathVariable("jobID") String jobID) {
		return "This is job: " + jobID;
	}
}
