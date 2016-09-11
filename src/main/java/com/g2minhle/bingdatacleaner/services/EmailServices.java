package com.g2minhle.bingdatacleaner.services;

import java.io.IOException;

import com.g2minhle.bingdatacleaner.model.Job;

public interface EmailServices {
	public void notifyDoneJobDone(Job job) throws IOException;
}
