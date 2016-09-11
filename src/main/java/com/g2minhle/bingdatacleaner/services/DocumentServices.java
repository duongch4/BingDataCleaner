package com.g2minhle.bingdatacleaner.services;

import java.util.List;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;

public interface DocumentServices {

	public Long getDocumentSize(String sourceDocumentId)
			throws CannotAccessToDocumentException, DocumentServiceConnectivityException;

	public String createDestinationDocument(String userEmail, Long totalWork)
			throws DocumentServiceConnectivityException;

	public String getDocumentIdFromUrl(String documentUrl)
			throws InvalidDocumentUrlException;

	public List<String> readFromDocument(String documentId, Long start, Long count)
			throws DocumentServiceConnectivityException;

	public void writeToDocument(
			String documentId,
			Long start,
			List<String> dataEntries,
			List<String> searchResults) throws DocumentServiceConnectivityException;
}