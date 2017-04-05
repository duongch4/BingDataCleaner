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

	private static final String DYNAMO_DB_TABLE_ENV_VAR = "DYNAMO_DB_TABLE";
	
	private static DynamoDBServices _instance = null;

	private static ObjectMapper _mapper;
	private static DynamoDB _dynamoDBInstance;
	private static Table _table;
	static {
		_mapper = new ObjectMapper();
		_dynamoDBInstance = new DynamoDB(new AmazonDynamoDBClient());
		_table = _dynamoDBInstance.getTable(System.getenv(DYNAMO_DB_TABLE_ENV_VAR));
	}

	private DynamoDBServices() {
		// Exists only to defeat instantiation.
	}

	public static DynamoDBServices getInstance() {
		if (_instance == null) {
			_instance = new DynamoDBServices();
		}
		return _instance;
	}

	@Override
	public Job getJob(String jobId)
			throws DatabaseConnectivityException, JobNotFoundException {
		try {
			PrimaryKey key = new PrimaryKey();
			key.addComponent("id", jobId);
			Item item = _table.getItem(key);
			if (item == null) {
				throw new JobNotFoundException();
			}
			return _mapper.readValue(item.toJSON(), Job.class);
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}
	}

	@Override
	public void saveJob(Job newJob) throws DatabaseConnectivityException {
		try {
			Item item = Item.fromJSON(_mapper.writeValueAsString(newJob));
			_table.putItem(item);
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}
	}

	@Override
	public List<Job> getJobs() throws DatabaseConnectivityException {
		try {
			List<Job> jobs = new LinkedList<Job>();
			ItemCollection<ScanOutcome> scanResults = _table.scan();
			for (Item item : scanResults) {
				jobs.add(_mapper.readValue(item.toJSON(), Job.class));
			}
			return jobs;
		} catch (Exception e) {
			throw new DatabaseConnectivityException(e.getMessage());
		}

	}

}
