package com.ag.poc.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.dao.Signature;
import com.ag.poc.dao.SignatureRepository;
import com.ag.poc.integration.esign.dto.DocSignRequest;
import com.ag.poc.integration.esign.dto.DocSignResponse;
import com.ag.poc.integration.esign.dto.SignerDTO;
import com.ag.poc.integration.esign.dto.SigningDocumentDTO;
import com.ag.poc.integration.esign.service.SignerProvider;
import com.ag.poc.integration.esign.service.SignerServiceFactory;
import com.ag.poc.integration.scrapping.service.DataPlatform;
import com.ag.poc.integration.scrapping.service.DocResponse;
import com.ag.poc.integration.scrapping.service.ScrappingFactory;
import com.ag.poc.integration.scrapping.service.ScrappingService;
import com.ag.poc.service.dto.GetDocumentResponse;

@Service
public class ESignerService {

	@Autowired
	private ScrappingFactory scrappingFactory;

	@Autowired
	private SignerServiceFactory signerServiceFactory;

	@Autowired
	private SignatureRepository signatureRepository;

	@Value("${app.baseurl}")
	private String appCallbackUrl;

	@Value("${document.download.location}")
	private String docDownloadLocation;

	public String scrapAndEsign(final String dealGuid) {
		ScrappingService scrappingService = this.scrappingFactory.getScrappingService(DataPlatform.dealhub);
		DocResponse docResponse = scrappingService.downloadDoc(dealGuid, docDownloadLocation);
		DocSignResponse docSignResponse = this.signerServiceFactory.getSignerService(SignerProvider.docusign)
				.eSignDoc(createDocSignRequest(docResponse), appCallbackUrl);
		Signature signature = new Signature();
		signature.setCreatedAt(new Date());
		signature.setExternalEnvId(docSignResponse.getEnvelopeId());
		signature.setDocId(docSignResponse.getDocId());
		signature.setDocName(docSignResponse.getDocName());
		this.signatureRepository.save(signature);
		return docSignResponse.getViewUrl();
	}

	public GetDocumentResponse downloadDoc(final Long id) {
		Signature signature = this.signatureRepository.findById(id).orElse(null);
		if (signature == null) {
			throw new RuntimeException("Invalid id");
		}
		byte[] data = this.signerServiceFactory.getSignerService(SignerProvider.docusign).getDoc(signature.getExternalEnvId(),
				signature.getDocId());
		return new GetDocumentResponse(data, signature.getDocName(), "application/pdf");
	}

	public List<Signature> findAll() {
		return signatureRepository.findAll();
	}

	private DocSignRequest createDocSignRequest(final DocResponse docResponse) {
		// Added dummy data should be ask from user
		SigningDocumentDTO signingDocumentDTO = new SigningDocumentDTO();
		signingDocumentDTO.setDocName(docResponse.getDocName());
		signingDocumentDTO.setFileExtension(StringUtils.substringAfterLast(docResponse.getDocLocation(), DealSignConstants.DOT));
		signingDocumentDTO.setDocId("1");
		signingDocumentDTO.setTabLabel("SignHereTab");
		signingDocumentDTO.setxPosition("195");
		signingDocumentDTO.setyPosition("147");
		signingDocumentDTO.setEnvelopeSubject("Please sign this document");
		signingDocumentDTO.setDocLocation(docResponse.getDocLocation());
		signingDocumentDTO.setPageNumber("7");
		SignerDTO signerDTO = new SignerDTO();
		signerDTO.setSignerName("John Signer");
		signerDTO.setSignerEmail("john.signer@example.com");
		signerDTO.setClientUserId("123");
		signerDTO.setRecipientStatus("sent");
		signerDTO.setRecipientId("1");
		DocSignRequest pdfSignRequest = new DocSignRequest(signingDocumentDTO, signerDTO);
		return pdfSignRequest;
	}
}
