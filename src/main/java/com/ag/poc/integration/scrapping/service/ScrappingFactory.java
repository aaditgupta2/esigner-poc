package com.ag.poc.integration.scrapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ag.poc.integration.scrapping.service.dealhub.DealHubDataScrappingService;

@Component
public class ScrappingFactory {

	@Autowired
	private DealHubDataScrappingService dealHubDataScrappingService;

	public ScrappingService getScrappingService(final DataPlatform dataPlatform) {
		if (DataPlatform.dealhub == dataPlatform) {
			return dealHubDataScrappingService;
		} else {
			throw new UnsupportedOperationException("We do not support: " + dataPlatform);
		}
	}

}
