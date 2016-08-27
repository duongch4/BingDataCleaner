package com.g2minhle.bingdatacleaner.services;

import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;

public interface DocumentServices {

	public Long getDocumentSize(String sourceDocumentId)
			throws CannotAccessToDocumentException, DocumentServiceConnectivityException;

	public String createDestinationDocument(String userEmail)
			throws DocumentServiceConnectivityException;

	public String getDocumentIdFromUrl(String documentUrl)
			throws InvalidDocumentUrlException;
}
