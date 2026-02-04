package com.streamspot.webapp.opengallary.dto;

public class EmailActivationRequest {
	
	private Long id;
	private String fromAddress;
	private String subject;
	private String body;
	
	
	public EmailActivationRequest() {}

	public EmailActivationRequest(Long id, String fromAddress, String subject, String body) {
		super();
		this.id = id;
		this.fromAddress = fromAddress;
		this.subject = subject;
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
