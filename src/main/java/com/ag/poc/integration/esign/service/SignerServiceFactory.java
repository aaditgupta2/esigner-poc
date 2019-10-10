package com.ag.poc.integration.esign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignerServiceFactory {

	@Autowired
	private DocuSignerService docuSignerService;

	public SignerService getSignerService(final SignerProvider signerProvider) {
		if (SignerProvider.docusign == signerProvider) {
			return docuSignerService;
		} else {
			throw new UnsupportedOperationException("We do not support: " + signerProvider);
		}
	}

}
