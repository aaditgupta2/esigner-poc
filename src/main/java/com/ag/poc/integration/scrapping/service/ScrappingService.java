package com.ag.poc.integration.scrapping.service;

public interface ScrappingService {

	public DocResponse downloadDoc(final String uuid, final String docDownloadLocation);
}
