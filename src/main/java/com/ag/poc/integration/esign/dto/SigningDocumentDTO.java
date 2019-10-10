package com.ag.poc.integration.esign.dto;

import java.io.Serializable;

public class SigningDocumentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String docLocation;

	private String docName;

	private String fileExtension;

	private String docId;

	private String pageNumber;

	private String tabLabel;

	private String xPosition;

	private String yPosition;

	private String envelopeSubject;

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

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getTabLabel() {
		return tabLabel;
	}

	public void setTabLabel(String tabLabel) {
		this.tabLabel = tabLabel;
	}

	public String getxPosition() {
		return xPosition;
	}

	public void setxPosition(String xPosition) {
		this.xPosition = xPosition;
	}

	public String getyPosition() {
		return yPosition;
	}

	public void setyPosition(String yPosition) {
		this.yPosition = yPosition;
	}

	public String getEnvelopeSubject() {
		return envelopeSubject;
	}

	public void setEnvelopeSubject(String envelopeSubject) {
		this.envelopeSubject = envelopeSubject;
	}

	@Override
	public String toString() {
		return "SigningDocumentDTO [docLocation=" + docLocation + ", docName=" + docName + ", fileExtension=" + fileExtension + ", docId="
				+ docId + ", pageNumber=" + pageNumber + ", tabLabel=" + tabLabel + ", xPosition=" + xPosition + ", yPosition=" + yPosition
				+ ", envelopeSubject=" + envelopeSubject + "]";
	}

}
