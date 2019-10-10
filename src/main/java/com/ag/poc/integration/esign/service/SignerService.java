package com.ag.poc.integration.esign.service;

import com.ag.poc.integration.esign.dto.DocSignRequest;
import com.ag.poc.integration.esign.dto.DocSignResponse;

public interface SignerService {

	public DocSignResponse eSignDoc(final DocSignRequest pdfSignRequest, final String callbackUrl);

	public byte[] getDoc(final String envelopeId, final String docId);

}
