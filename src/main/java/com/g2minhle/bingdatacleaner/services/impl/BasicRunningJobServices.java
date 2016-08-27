package com.g2minhle.bingdatacleaner.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.JobServices.UserAction;
import com.g2minhle.bingdatacleaner.services.RunningJobServices;

public class BasicRunningJobServices implements RunningJobServices {

	Map<String, Thread> threads = new HashMap<>();
	
	@Override
	public void startJob(Job newJob) {
		
		
	}

	@Override
	public void performActionOnJob(Job job, UserAction action) {
		// TODO Auto-generated method stub

	}
}
