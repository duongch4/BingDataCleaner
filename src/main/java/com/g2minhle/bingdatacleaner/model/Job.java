package com.g2minhle.bingdatacleaner.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Job {

	public enum JobStatus {
		CREATED, RUNNING, PAUSED, DONE, CANCELLED
	}

	String _id;
	String _userEmail;
	String _sourceDocumentId;
	String _destinationDocumentId;
	JobStatus _status;
	Long _totalWork;
	Long _progress;
	Date _createdTime;
	Date _doneTime;

	public static class JobbBuilder {
		String _id;
		String _userEmail;
		String _sourceDocumentId;
		String _destinationDocumentId;
		JobStatus _status;
		Long _totalWork;
		Long _progress;
		Date _createdTime;
		Date _doneTime;

		public JobbBuilder withId(String id) {
			_id = id;
			return this;
		}

		public JobbBuilder withUserEmail(String userEmail) {
			_userEmail = userEmail;
			return this;
		}

		public JobbBuilder withSourceDocumentId(String sourceDocumentId) {
			_sourceDocumentId = sourceDocumentId;
			return this;
		}
		
		public JobbBuilder withDestincationDocumentId(String destinationDocumentId) {
			_destinationDocumentId = destinationDocumentId;
			return this;
		}

		public JobbBuilder withStatus(JobStatus status) {
			_status = status;
			return this;
		}

		public JobbBuilder withTotalWork(Long totalWork) {
			_totalWork = totalWork;
			return this;
		}

		public JobbBuilder withProgress(Long progress) {
			_progress = progress;
			return this;
		}

		public JobbBuilder withCreatedTime(Date createdTime) {
			_createdTime = createdTime;
			return this;
		}

		public JobbBuilder withDoneTime(Date doneTime) {
			_doneTime = doneTime;
			return this;
		}

		public JobbBuilder withJob(Job job) {
			_id = job.getId();
			_userEmail = job.getUserEmail();
			_sourceDocumentId = job.getSourceDocumentId();
			_status = job.getStatus();
			_totalWork = job.getTotalWork();
			_progress = job.getProgress();
			_createdTime = job.getCreatedTime();
			_doneTime = job.getDoneTime();
			return this;
		}

		public Job build() {
			return new Job(
					_id,
					_userEmail,
					_sourceDocumentId,
					_destinationDocumentId,
					_status,
					_totalWork,
					_progress,
					_createdTime,
					_doneTime);
		}
	}

	@JsonCreator
	public Job(
			@JsonProperty("id")
			String id,
			@JsonProperty("userEmail")
			String userEmail,
			@JsonProperty("sourceDocumentId")
			String sourceDocumentId,
			@JsonProperty("destinationDocumentId")
			String destinationDocumentId,
			@JsonProperty("status")
			JobStatus status,
			@JsonProperty("totalWork")
			Long totalWork,
			@JsonProperty("progress")
			Long progress,
			@JsonProperty("createdTime")
			Date createdTime,
			@JsonProperty("doneTime")
			Date doneTime) {
		_id = id;
		_userEmail = userEmail;
		_sourceDocumentId = sourceDocumentId;
		_destinationDocumentId = destinationDocumentId;
		_status = status;
		_totalWork = totalWork;
		_progress = progress;
		_createdTime = createdTime;
		_doneTime = doneTime;
	}

	public String getId() {
		return _id;
	}

	public String getUserEmail() {
		return _userEmail;
	}

	public String getSourceDocumentId() {
		return _sourceDocumentId;
	}
	
	public String getDestinationDocumentId() {
		return _destinationDocumentId;
	}


	public JobStatus getStatus() {
		return _status;
	}

	public Long getTotalWork() {
		return _totalWork;
	}

	public Long getProgress() {
		return _progress;
	}

	public Date getCreatedTime() {
		return _createdTime;
	}

	public Date getDoneTime() {
		return _doneTime;
	}
}
