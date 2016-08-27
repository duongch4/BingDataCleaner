package com.g2minhle.bingdatacleaner.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.g2minhle.bingdatacleaner.BingDataCleanerApplication;
import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;

public class GoogleSheetServices implements DocumentServices {

	static Drive driveServices = null;
	static Sheets sheetServices = null;
	static List<String> scopes = new ArrayList<String>();
	static final String parentFolderId = "0B65PxeIkFoQIZTFaMGRqX1JkUGs";
	static {
		scopes.addAll(SheetsScopes.all());
		scopes.addAll(DriveScopes.all());
	}

	/** Global instance of the HTTP transport. */
	static HttpTransport HTTP_TRANSPORT;
	static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public GoogleSheetServices() {
		if (driveServices != null && sheetServices != null) {
			return;
		}

		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException t) {
			// TODO add alert
			t.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Add alert
			e.printStackTrace();
			return;
		}
		InputStream in =
				GoogleSheetServices.class.getResourceAsStream("/client_secret.json");
		GoogleCredential credential;
		try {
			credential = GoogleCredential.fromStream(in).createScoped(scopes);
		} catch (Exception e) {
			// TODO add alert
			e.printStackTrace();
			return;
		}

		sheetServices =
				new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(BingDataCleanerApplication.APPLICATION_NAME)
						.build();
		driveServices =
				new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(BingDataCleanerApplication.APPLICATION_NAME)
						.build();
	}

	@Override
	public Long getDocumentSize(String sourceDocumentId)
			throws CannotAccessToDocumentException, DocumentServiceConnectivityException {
		return 10L;
	}

	@Override
	public String createDestinationDocument(String userEmail)
			throws DocumentServiceConnectivityException {
		File newSheet = new File();
		newSheet.setMimeType("application/vnd.google-apps.spreadsheet");
		newSheet.setParents(Arrays.asList(parentFolderId));
		newSheet.setName("temp");
		Permission userPermission =
				new Permission().setType("user").setRole("writer")
						.setEmailAddress(userEmail);
		try {
			newSheet = driveServices.files().create(newSheet).execute();
			newSheet.setName(newSheet.getId());
			File changedSheetName = new File();
			changedSheetName.setName(newSheet.getId());
			driveServices.files().update(newSheet.getId(), changedSheetName).execute();			
			driveServices.permissions().create(newSheet.getId(), userPermission).execute();
		} catch (Exception e) {
			throw new DocumentServiceConnectivityException(e.getMessage());
		}
		return newSheet.getId();
	}

	@Override
	public String getDocumentIdFromUrl(String documentUrl)
			throws InvalidDocumentUrlException {
		// TODO Auto-generated method stub
		return null;
	}

}
