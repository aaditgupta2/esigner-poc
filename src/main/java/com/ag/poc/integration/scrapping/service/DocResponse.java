package com.ag.poc.integration.scrapping.service;

public class DocResponse {

	private String docLocation;

	private String docName;

	public DocResponse(String docLocation, String docName) {
		super();
		this.docLocation = docLocation;
		this.docName = docName;
	}

	public DocResponse() {
		super();
	}

	public String getDocLocation() {
		return docLocation;
	}

	public void setDocLocation(String docLocation) {
		this.docLocation = docLocation;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

}
