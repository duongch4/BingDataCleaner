package com.g2minhle.bingdatacleaner.services.impl;

import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2minhle.bingdatacleaner.exception.DatabaseConnectivityException;
import com.g2minhle.bingdatacleaner.exception.JobNotFoundException;
import com.g2minhle.bingdatacleaner.model.Job;
import com.g2minhle.bingdatacleaner.services.DatabaseServices;

public class DynamoDBServices implements DatabaseServices {

	static ObjectMapper Mapper = new ObjectMapper();
	static DynamoDB DynamoDBInstance = new DynamoDB(new AmazonDynamoDBClient());
	static Table Table = DynamoDBInstance.getTable("BingDataCleaner");

	private static DynamoDBServices instance = null;

	private DynamoDBServices() {
		// Exists only to defeat instantiation.
	}

	public static DynamoDBServices getInstance() {
		if (instance == null) {
			instance = new DynamoDBServices();
		}
		return instance;
	}

	@Override
	public Job getJob(String jobId)
			throws DatabaseConnectivityException, JobNotFoundException {
		try {
			// TODO Auto-generated method stub
			PrimaryKey key = new PrimaryKey();
			key.addComponent("id", jobId);
			Item item = Table.getItem(key);
			if (item == null) {
				throw new JobNotFoundException();
			}
			return Mapper.readValue(item.toJSON(), Job.class);
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}
	}

	@Override
	public void saveJob(Job newJob) throws DatabaseConnectivityException {
		try {
			Item item = Item.fromJSON(Mapper.writeValueAsString(newJob));
			Table.putItem(item);
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}
	}

	@Override
	public List<Job> getJobs() throws DatabaseConnectivityException {
		try {
			List<Job> jobs = new LinkedList<>();
			ItemCollection<ScanOutcome> scanResults = Table.scan();
			for (Item item : scanResults) {
				jobs.add(Mapper.readValue(item.toJSON(), Job.class));
			}
			return jobs;
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}
		
	}

}
