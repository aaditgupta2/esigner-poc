package com.ag.poc.integration.scrapping.service.dealhub;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.integration.scrapping.service.DocResponse;

public class DealHubClient {

	@Autowired
	private DealhubWSHandler stringDealhubWSHandler;

	@Autowired
	private Mapper pdfProcessorMapper;

	@Value("${dealhub.valooto.print.document.token.url}")
	private String dealhubPrintDocTokenUrl;

	@Value("${dealhub.valooto.init.auth.token.url}")
	private String dealhubInitAuthTokenUrl;

	@Value("${dealhub.valooto.deal.document.url}")
	private String dealhubDealDocumentUrl;

	@Value("${dealhub.valooto.printable.document.url}")
	private String dealhubprintableDocumentUrl;

	@Value("${dealhub.valooto.printable.document.download.location}")
	private String dealhubPrintableDocumentLocation;

	public String getPrintableDocumentToken(String dealGuid, final String docGuid, final String apiAuthToken) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set(DealSignConstants.COOKIE, apiAuthToken);
		return stringDealhubWSHandler.invokeGetCallAndProvideResponseBody(MessageFormat.format(dealhubPrintDocTokenUrl, dealGuid, docGuid),
				headers);

	}

	public DocResponse getPrintableDocument(String documentToken, final String apiAuthToken)
			throws IOException, RestClientException, URISyntaxException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set(HttpHeaders.COOKIE, apiAuthToken);
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		return stringDealhubWSHandler.invokeGetCallAndDownloadFile(MessageFormat.format(dealhubprintableDocumentUrl, documentToken),
				dealhubPrintableDocumentLocation, headers);

	}

	public String getAuthToken() {
		ResponseEntity<String> response = stringDealhubWSHandler.invokeGetCall(dealhubInitAuthTokenUrl);
		return pdfProcessorMapper.mapResponseAndGetAuthToken(response.getHeaders());
	}

	public String getDocGuid(String authToken) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set(DealSignConstants.COOKIE, authToken);
		return pdfProcessorMapper.getDocGuid(stringDealhubWSHandler.invokeGetCallAndProvideResponseBody(dealhubDealDocumentUrl, headers));
	}

}
