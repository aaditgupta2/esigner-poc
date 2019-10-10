package com.ag.poc.integration.esign.dto;

import java.io.Serializable;

public class SignerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String signerName;

	private String signerEmail;

	private String clientUserId;

	private String recipientId;

	private String recipientStatus;

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getSignerEmail() {
		return signerEmail;
	}

	public void setSignerEmail(String signerEmail) {
		this.signerEmail = signerEmail;
	}

	public String getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(String clientUserId) {
		this.clientUserId = clientUserId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getRecipientStatus() {
		return recipientStatus;
	}

	public void setRecipientStatus(String recipientStatus) {
		this.recipientStatus = recipientStatus;
	}

	@Override
	public String toString() {
		return "RecipientDTO [signerName=" + signerName + ", signerEmail=" + signerEmail + ", clientUserId=" + clientUserId
				+ ", recipientId=" + recipientId + ", recipientStatus=" + recipientStatus + "]";
	}

}
