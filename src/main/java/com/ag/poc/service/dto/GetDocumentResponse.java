package com.ag.poc.service.dto;

public class GetDocumentResponse {

	private byte[] data;

	private String fileName;

	private String contentType;

	public GetDocumentResponse(byte[] data, String fileName, String contentType) {
		super();
		this.data = data;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	public GetDocumentResponse() {
		super();
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
