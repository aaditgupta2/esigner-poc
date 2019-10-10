package com.ag.poc.integration.esign.dto;

public class DocSignResponse {

	private String viewUrl;

	private String envelopeId;

	private String docId;

	private String docName;

	public DocSignResponse(String viewUrl, String envelopeId, String docId, String docName) {
		super();
		this.viewUrl = viewUrl;
		this.envelopeId = envelopeId;
		this.docId = docId;
		this.docName = docName;
	}

	public DocSignResponse() {
		super();
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getEnvelopeId() {
		return envelopeId;
	}

	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

}
