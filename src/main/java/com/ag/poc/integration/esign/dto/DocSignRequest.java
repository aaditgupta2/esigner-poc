package com.ag.poc.integration.esign.dto;

import java.io.Serializable;

public class DocSignRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private SigningDocumentDTO signingDocumentDTO;

	private SignerDTO signerDTO;

	public DocSignRequest(final SigningDocumentDTO signingDocumentDTO, final SignerDTO signerDTO) {
		super();
		this.signingDocumentDTO = signingDocumentDTO;
		this.signerDTO = signerDTO;
	}

	public SigningDocumentDTO getSigningDocumentDTO() {
		return signingDocumentDTO;
	}

	public void setSigningDocumentDTO(SigningDocumentDTO signingDocumentDTO) {
		this.signingDocumentDTO = signingDocumentDTO;
	}

	public SignerDTO getSignerDTO() {
		return signerDTO;
	}

	public void setSignerDTO(SignerDTO signerDTO) {
		this.signerDTO = signerDTO;
	}

	@Override
	public String toString() {
		return "PDFSignRequest [signingDocumentDTO=" + signingDocumentDTO + ", SignerDTO=" + signerDTO + "]";
	}

}
