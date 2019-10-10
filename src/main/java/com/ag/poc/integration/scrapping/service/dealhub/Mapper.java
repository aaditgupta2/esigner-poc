package com.ag.poc.integration.scrapping.service.dealhub;

import org.springframework.http.HttpHeaders;

public interface Mapper {

	public String mapResponseAndGetAuthToken(HttpHeaders httpHeaders);

	public String getDocGuid(String response);
}
