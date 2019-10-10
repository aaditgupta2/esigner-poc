package com.ag.poc.integration.scrapping.service.dealhub;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.integration.scrapping.service.DocResponse;
import com.ag.poc.integration.scrapping.service.ScrappingService;

@Service
public class DealHubDataScrappingService implements ScrappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DealHubDataScrappingService.class);

	@Autowired
	private PDFProcessorWSHandler pdfProcessorWSHandler;

	@Override
	public DocResponse downloadDoc(final String dealGuid, final String downloadDocLocation) {
		if (StringUtils.isEmpty(dealGuid)) {
			throw new RuntimeException("dealGuid cannot be empty");
		}
		String apiAuthToken = pdfProcessorWSHandler.getAuthToken();
		LOGGER.debug("apiAuthToken is : {}", apiAuthToken);
		String docGuid = pdfProcessorWSHandler.getDocGuid(apiAuthToken);
		LOGGER.debug("docGuid is : {}", docGuid);
		String documentToken = pdfProcessorWSHandler.getPrintableDocumentToken(dealGuid, docGuid, apiAuthToken);
		LOGGER.debug("Doc Download Token is : {}", documentToken);
		return pdfProcessorWSHandler.getPrintableDocument(processDocToken(documentToken), apiAuthToken, downloadDocLocation);
	}

	private String processDocToken(String documentToken) {
		return StringUtils.remove(documentToken, DealSignConstants.DOUBLE_QUOTE);
	}

}
