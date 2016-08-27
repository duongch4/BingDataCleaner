package com.g2minhle.bingdatacleaner.services.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
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

	@Override
	public Job getJob(String jobId) throws DatabaseConnectivityException, JobNotFoundException{
		try {
			// TODO Auto-generated method stub
			PrimaryKey key = new PrimaryKey();
			key.addComponent("id", jobId);
			Item item = Table.getItem(key);
			if (item == null){
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

}
