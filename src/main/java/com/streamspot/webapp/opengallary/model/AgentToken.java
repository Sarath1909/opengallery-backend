package com.streamspot.webapp.opengallary.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(AgentTokenId.class)
@Table(name="agent_token", schema="opengallery")
public class AgentToken {
	
	@Id
	@Column(name="recipient_id", nullable=false)
	private long recipientId;
	@Id
	@Column(name="token_type", nullable=false)
	private String tokenType;
	@Id
	@Column(name="token", nullable=false)
	private String token;
	@Column(name="tokenExpiry", nullable=false)
	private Instant tokenExpiry;
	@Column(name="created_time", nullable=false, updatable=false)
	private Instant createdTime;
	
	public long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getTokenExpiry() {
		return tokenExpiry;
	}
	public void setTokenExpiry(Instant tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}
	public Instant getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}	
	
}
