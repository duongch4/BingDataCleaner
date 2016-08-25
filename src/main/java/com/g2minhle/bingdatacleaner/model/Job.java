package com.g2minhle.bingdatacleaner.model;

import java.util.Date;

public class Job {

	public enum JobStatus {
		CREATED, RUNNING, PAUSED, DONE, CANCELLED
	}

	String _id;
	String _userEmail;
	String _documentID;
	JobStatus _status;
	Long _totalWork;
	Long _progress;
	Date _createdTime;
	Date _doneTime;

	public static class JobbBuilder {
		String _id;
		String _userEmail;
		String _documentID;
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

		public JobbBuilder withDocumentID(String documentID) {
			_documentID = documentID;
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
			_documentID = job.getDocumentID();
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
					_documentID,
					_status,
					_totalWork,
					_progress,
					_createdTime,
					_doneTime);
		}
	}

	public Job(
			String id,
			String userEmail,
			String documentID,
			JobStatus status,
			Long totalWork,
			Long progress,
			Date createdTime,
			Date doneTime) {
		_id = id;
		_userEmail = userEmail;
		_documentID = documentID;
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

	public String getDocumentID() {
		return _documentID;
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
