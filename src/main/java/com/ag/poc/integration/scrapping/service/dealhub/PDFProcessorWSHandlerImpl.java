package com.ag.poc.integration.scrapping.service.dealhub;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.integration.scrapping.service.DocResponse;

@Component
public class PDFProcessorWSHandlerImpl implements PDFProcessorWSHandler {

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

	private static final Logger LOGGER = LoggerFactory.getLogger(PDFProcessorWSHandlerImpl.class);

	@Override
	public String getPrintableDocumentToken(String dealGuid, final String docGuid, final String apiAuthToken) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set(DealSignConstants.COOKIE, apiAuthToken);
		return stringDealhubWSHandler.invokeGetCallAndProvideResponseBody(MessageFormat.format(dealhubPrintDocTokenUrl, dealGuid, docGuid),
				headers);
	}

	@Override
	public DocResponse getPrintableDocument(String documentToken, final String apiAuthToken, final String downloadDoc) {
		try {
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.set(HttpHeaders.COOKIE, apiAuthToken);
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM_VALUE);
			return stringDealhubWSHandler.invokeGetCallAndDownloadFile(MessageFormat.format(dealhubprintableDocumentUrl, documentToken),
					downloadDoc, headers);
		} catch (IOException | RestClientException | URISyntaxException e) {
			LOGGER.error("getPrintableDocument failed {}", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getAuthToken() {
		ResponseEntity<String> response = stringDealhubWSHandler.invokeGetCall(dealhubInitAuthTokenUrl);
		return pdfProcessorMapper.mapResponseAndGetAuthToken(response.getHeaders());
	}

	@Override
	public String getDocGuid(String authToken) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.set(DealSignConstants.COOKIE, authToken);
		return pdfProcessorMapper.getDocGuid(stringDealhubWSHandler.invokeGetCallAndProvideResponseBody(dealhubDealDocumentUrl, headers));
	}

}
