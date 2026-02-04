package com.streamspot.webapp.opengallary.dto;

public class ExternalMediaResponse {
	
	private boolean status;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ExternalMediaResponse [status=" + status + "]";
	}
	
}
