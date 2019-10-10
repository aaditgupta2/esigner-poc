package com.ag.poc.integration.scrapping.service.dealhub;

import com.ag.poc.integration.scrapping.service.DocResponse;

public interface PDFProcessorWSHandler {

	public String getPrintableDocumentToken(final String dealGuid, final String docGuid, final String apiAuthToken);

	public DocResponse getPrintableDocument(final String documentToken, final String apiAuthToken, final String downloadDoc);

	public String getAuthToken();

	public String getDocGuid(String authToken);

}
