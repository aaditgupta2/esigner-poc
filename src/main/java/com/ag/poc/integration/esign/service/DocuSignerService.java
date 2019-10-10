package com.ag.poc.integration.esign.service;

import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.integration.esign.dto.DocSignRequest;
import com.ag.poc.integration.esign.dto.DocSignResponse;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;
import com.sun.jersey.core.util.Base64;

@Service
public class DocuSignerService implements SignerService {

	@Value("${docusign.accesstoken}")
	private String accessToken;

	@Value("${docusign.accountId}")
	private String accountId;

	@Value("${docusign.baseurl}")
	private String docusignBasePath;

	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignerService.class);

	@Override
	public DocSignResponse eSignDoc(final DocSignRequest docSignRequest, final String callbackUrl) {
		EnvelopesApi envelopesApi = getenvelopeApi();
		Document document = getDocuSignDocument(docSignRequest);
		Signer signer = getSigner(docSignRequest);
		EnvelopeDefinition envelopeDefinition = getEnvelopeDefinition(docSignRequest, document, signer);
		String envelopeId = createEnvelope(envelopeDefinition, envelopesApi);
		ViewUrl viewUrl = createRecipientView(docSignRequest, callbackUrl, envelopeId, envelopesApi);
		String redirectUrl = viewUrl.getUrl();
		return new DocSignResponse(redirectUrl, envelopeId, document.getDocumentId(), docSignRequest.getSigningDocumentDTO().getDocName());
	}

	private ViewUrl createRecipientView(final DocSignRequest docSignRequest, final String callbackUrl, final String envelopeId,
			final EnvelopesApi envelopesApi) {
		try {
			RecipientViewRequest viewRequest = getRecipientViewRequest(docSignRequest, callbackUrl);
			ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
			return viewUrl;
		} catch (ApiException e) {
			LOGGER.error("EnvelopesApi Exception {}", e);
			throw new RuntimeException(e);
		}
	}

	private String createEnvelope(final EnvelopeDefinition envelopeDefinition, final EnvelopesApi envelopesApi) {
		try {
			EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
			return results.getEnvelopeId();
		} catch (ApiException e) {
			LOGGER.error("EnvelopesApi Exception {}", e);
			throw new RuntimeException(e);
		}
	}

	private RecipientViewRequest getRecipientViewRequest(final DocSignRequest docSignRequest, final String callbackUrl) {
		RecipientViewRequest viewRequest = new RecipientViewRequest();
		viewRequest.setReturnUrl(callbackUrl + DealSignConstants.SLASH);
		viewRequest.setAuthenticationMethod(DealSignConstants.NONE);
		viewRequest.setEmail(docSignRequest.getSignerDTO().getSignerEmail());
		viewRequest.setUserName(docSignRequest.getSignerDTO().getSignerName());
		viewRequest.setClientUserId(docSignRequest.getSignerDTO().getClientUserId());
		return viewRequest;
	}

	private Document getDocuSignDocument(final DocSignRequest docSignRequest) {
		Document document = new Document();
		byte[] buffer = Utils.readFile(docSignRequest.getSigningDocumentDTO().getDocLocation());
		String docBase64 = new String(Base64.encode(buffer));
		document.setDocumentBase64(docBase64);
		document.setName(docSignRequest.getSigningDocumentDTO().getDocName()); // can be different from actual file name
		document.setFileExtension(docSignRequest.getSigningDocumentDTO().getFileExtension()); // many different document types are accepted
		document.setDocumentId(docSignRequest.getSigningDocumentDTO().getDocId()); // a label used to reference the doc
		return document;
	}

	private Signer getSigner(final DocSignRequest docSignRequest) {
		Signer signer = new Signer();
		signer.setEmail(docSignRequest.getSignerDTO().getSignerEmail());
		signer.setName(docSignRequest.getSignerDTO().getSignerName());
		signer.clientUserId(docSignRequest.getSignerDTO().getClientUserId());
		signer.recipientId(docSignRequest.getSignerDTO().getRecipientId());

		SignHere signHere = new SignHere();
		signHere.setDocumentId(docSignRequest.getSigningDocumentDTO().getDocId());
		signHere.setPageNumber(docSignRequest.getSigningDocumentDTO().getPageNumber());
		signHere.setRecipientId(docSignRequest.getSignerDTO().getRecipientId());
		signHere.setTabLabel(docSignRequest.getSigningDocumentDTO().getTabLabel());
		signHere.setXPosition(docSignRequest.getSigningDocumentDTO().getxPosition());
		signHere.setYPosition(docSignRequest.getSigningDocumentDTO().getyPosition());

		Tabs signerTabs = new Tabs();
		signerTabs.setSignHereTabs(Arrays.asList(signHere));
		signer.setTabs(signerTabs);
		return signer;
	}

	private EnvelopeDefinition getEnvelopeDefinition(final DocSignRequest docSignRequest, final Document document, final Signer signer) {
		EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
		envelopeDefinition.setEmailSubject(docSignRequest.getSigningDocumentDTO().getEnvelopeSubject());
		envelopeDefinition.setDocuments(Arrays.asList(document));
		Recipients recipients = new Recipients();
		recipients.setSigners(Arrays.asList(signer));
		envelopeDefinition.setRecipients(recipients);
		envelopeDefinition.setStatus(docSignRequest.getSignerDTO().getRecipientStatus()); // requests that the envelope be created and sent.
		return envelopeDefinition;
	}

	private EnvelopesApi getenvelopeApi() {
		ApiClient apiClient = new ApiClient(docusignBasePath);
		apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, DealSignConstants.BEARER + accessToken);
		return new EnvelopesApi(apiClient);
	}

	@Override
	public byte[] getDoc(final String envelopeId, final String documentId) {
		try {
			EnvelopesApi envelopesApi = getenvelopeApi();
			return envelopesApi.getDocument(accountId, envelopeId, documentId);
		} catch (ApiException e) {
			LOGGER.error("getDoc Exception {}", e);
			throw new RuntimeException(e);
		}
	}
}
