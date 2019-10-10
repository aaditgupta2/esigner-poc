package com.ag.poc.integration.scrapping.service.dealhub;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.integration.scrapping.service.DocResponse;

@Component
public class DealhubWSHandler {

	@Autowired
	private RestTemplateBuilder restTemplate;

	public ResponseEntity<String> invokeGetCall(final String uri) {
		return invokeGetCall(uri, null);
	}

	public ResponseEntity<String> invokeGetCall(final String uri, MultiValueMap<String, String> headers) {
		HttpEntity<MultiValueMap<String, String>> httpEntiry = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.build().exchange(uri, HttpMethod.GET, httpEntiry, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response;
		}
		return null;
	}

	public String invokeGetCallAndProvideResponseBody(final String uri) {
		return invokeGetCallAndProvideResponseBody(uri, null);
	}

	public String invokeGetCallAndProvideResponseBody(final String uri, MultiValueMap<String, String> headers) {
		HttpEntity<MultiValueMap<String, String>> httpEntiry = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.build().exchange(uri, HttpMethod.GET, httpEntiry, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		return null;
	}

	public DocResponse invokeGetCallAndDownloadFile(final String uri, String locationToDownload, MultiValueMap<String, String> headers)
			throws IOException, RestClientException, URISyntaxException {
		HttpEntity<MultiValueMap<String, String>> httpEntiry = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.build().exchange(new URI(uri), HttpMethod.GET, httpEntiry, byte[].class);
		List<String> contentDispositionList = response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION);
		String fileNameFromResponseStr = processResponseAndGetFileName(contentDispositionList);
		String fileLocation = StringUtils.join(new String[] { locationToDownload, fileNameFromResponseStr }, DealSignConstants.SLASH);
		Files.write(Paths.get(fileLocation), response.getBody());
		return new DocResponse(fileLocation, fileNameFromResponseStr);

	}

	private String processResponseAndGetFileName(List<String> contentDispositionList) {
		String fileNameFromResponseStr = "";
		if (CollectionUtils.isNotEmpty(contentDispositionList)) {
			Optional<String> fileNameFromResponse = contentDispositionList.stream()
					.filter(item -> item.contains(DealSignConstants.FILE_NAME)).findFirst();
			if (fileNameFromResponse.isPresent()) {
				List<String> contentDisposition = Arrays.asList(fileNameFromResponse.get().split((String) DealSignConstants.SEMICOLON));
				if (CollectionUtils.isNotEmpty(contentDisposition)) {
					for (String item : contentDisposition) {
						if (item.contains(DealSignConstants.FILE_NAME)) {
							fileNameFromResponseStr = StringUtils.substringAfter(StringUtils.remove(item, DealSignConstants.DOUBLE_QUOTE),
									DealSignConstants.FILE_NAME_IDENTIFIER);
						}
					}
				}
			}
		}
		return fileNameFromResponseStr;
	}

}
