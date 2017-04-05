package com.g2minhle.bingdatacleaner.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.g2minhle.bingdatacleaner.BingDataCleanerApplication;
import com.g2minhle.bingdatacleaner.exception.CannotAccessToDocumentException;
import com.g2minhle.bingdatacleaner.exception.DocumentServiceConnectivityException;
import com.g2minhle.bingdatacleaner.exception.InvalidDocumentUrlException;
import com.g2minhle.bingdatacleaner.services.DocumentServices;
import com.g2minhle.bingdatacleaner.services.NotificationServices;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;

public class GoogleSheetServices implements DocumentServices {

	private static final String GOOGLE_CLIENT_SECRET_ENV_VAR = "GOOGLE_CLIENT_SECRET";
	private static final String PARENT_FOLDER_ID_ENV_VAR = "PARENT_FOLDER_ID";
	private static final String DOC_SIZE_SCRIPT_URL_ENV_VAR = "DOC_SIZE_SCRIPT";
	
	@Autowired
	NotificationServices _notificationServices;
	
	private static GoogleSheetServices _instance = null;

	private static Drive _driveServices = null;
	private static Sheets _sheetServices = null;
	private static List<String> _googleAPIScopes = new ArrayList<String>();
	private static String _parentFolderId;
	private static String _docSizeScriptUrl;
	
	static {
		_googleAPIScopes.addAll(SheetsScopes.all());
		_googleAPIScopes.addAll(DriveScopes.all());
	}

	public static GoogleSheetServices getInstance() {
		if (_instance == null) {
			_instance = new GoogleSheetServices();
		}
		return _instance;
	}

	/** Global instance of the HTTP transport. */
	static HttpTransport HTTP_TRANSPORT;
	static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private GoogleSheetServices() {
		if (_driveServices != null && _sheetServices != null) {
			return;
		}

		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException t) {
			_notificationServices.alert(t.getMessage());
			return;
		} catch (IOException e) {
			_notificationServices.alert(e.getMessage());
			return;
		}
		String clientSecret = System.getenv(GOOGLE_CLIENT_SECRET_ENV_VAR);
		_parentFolderId = System.getenv(PARENT_FOLDER_ID_ENV_VAR);
		_docSizeScriptUrl = System.getenv(DOC_SIZE_SCRIPT_URL_ENV_VAR);
		
		InputStream in = new ByteArrayInputStream(clientSecret.getBytes());
		GoogleCredential credential;
		try {
			credential = GoogleCredential.fromStream(in).createScoped(_googleAPIScopes);
		} catch (Exception e) {
			_notificationServices.alert(e.getMessage());
			return;
		}

		_sheetServices =
				new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(BingDataCleanerApplication.APPLICATION_NAME)
						.build();
		_driveServices =
				new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(BingDataCleanerApplication.APPLICATION_NAME)
						.build();
	}

	@Override
	public Long getDocumentSize(String sourceDocumentId)
			throws CannotAccessToDocumentException, DocumentServiceConnectivityException {
		try {
			Document doc =
					Jsoup.connect(String.format(_docSizeScriptUrl, sourceDocumentId))
							.timeout(10000).ignoreHttpErrors(true)
							.userAgent("Mozilla/5.0").get();
			return Long.parseLong(doc.select("body").html().toString());
		} catch (Exception e) {
			throw new DocumentServiceConnectivityException(e.getMessage());
		}

	}

	@Override
	public String createDestinationDocument(String userEmail, Long totalWork)
			throws DocumentServiceConnectivityException {
		File newSheet = new File();
		newSheet.setMimeType("application/vnd.google-apps.spreadsheet");
		newSheet.setParents(Arrays.asList(_parentFolderId));
		newSheet.setName("temp");
		Permission userPermission =
				new Permission().setType("user").setRole("writer")
						.setEmailAddress(userEmail);
		try {
			newSheet = _driveServices.files().create(newSheet).execute();
			newSheet.setName(newSheet.getId());
			File changedSheetName = new File();
			changedSheetName.setName(newSheet.getId());
			_driveServices.files().update(newSheet.getId(), changedSheetName).execute();
			_driveServices.permissions().create(newSheet.getId(), userPermission)
					.execute();
			extendSheetSize(newSheet, totalWork);
		} catch (Exception e) {
			throw new DocumentServiceConnectivityException(e.getMessage());
		}
		return newSheet.getId();
	}

	@Override
	public String getDocumentIdFromUrl(String documentUrl)
			throws InvalidDocumentUrlException {
		documentUrl =
				documentUrl.substring("https://docs.google.com/spreadsheets/d/".length());
		documentUrl = documentUrl.substring(0, documentUrl.indexOf("/edit"));
		return documentUrl;
	}

	public List<String> readFromDocument(String documentId, Long start, Long count)
			throws DocumentServiceConnectivityException {
		try {
			List<String> result = new LinkedList<String>();
			String range = String.format("A%d:A%d", start + 1, start + count + 1);
			List<List<Object>> values =
					_sheetServices.spreadsheets().values().get(documentId, range)
							.execute().getValues();

			for (List<Object> row : values) {
				result.add(row.get(0).toString());
			}
			return result;
		} catch (IOException e) {
			throw new DocumentServiceConnectivityException(e.getMessage());
		}
	}

	public void writeToDocument(
			String documentId,
			Long start,
			List<String> dataEntries,
			List<String> searchResults) throws DocumentServiceConnectivityException {
		try {
			List<Request> requests = new ArrayList<Request>();

			for (int i = 0; i < dataEntries.size(); i++) {

				List<CellData> values = new ArrayList<CellData>();
				values.add(
						new CellData().setUserEnteredValue(
								new ExtendedValue().setStringValue(dataEntries.get(i))));
				values.add(
						new CellData().setUserEnteredValue(
								new ExtendedValue()
										.setStringValue(searchResults.get(i))));

				requests.add(
						new Request().setUpdateCells(
								new UpdateCellsRequest()
										.setStart(
												new GridCoordinate().setSheetId(0)
														.setRowIndex(start.intValue() + i)
														.setColumnIndex(0))
										.setRows(
												Arrays.asList(
														new RowData().setValues(values)))
										.setFields("userEnteredValue")));

			}

			BatchUpdateSpreadsheetRequest batchUpdateRequest =
					new BatchUpdateSpreadsheetRequest().setRequests(requests);
			_sheetServices.spreadsheets().batchUpdate(documentId, batchUpdateRequest)
					.execute();

		} catch (IOException e) {
			throw new DocumentServiceConnectivityException(e.getMessage());
		}
	}

	private void extendSheetSize(File newSheet, Long totalWork) throws IOException {
		BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest();
		InsertDimensionRequest insertDimensionRequest = new InsertDimensionRequest();
		DimensionRange range = new DimensionRange();
		range.setDimension("ROWS");
		range.setStartIndex(0);
		range.setSheetId(0);
		range.setEndIndex(totalWork.intValue());
		Request request = new Request();
		insertDimensionRequest.setRange(range);
		request.setInsertDimension(insertDimensionRequest);
		batchRequest.setRequests(Arrays.asList(request));
		_sheetServices.spreadsheets().batchUpdate(newSheet.getId(), batchRequest)
				.execute();
	}

}
