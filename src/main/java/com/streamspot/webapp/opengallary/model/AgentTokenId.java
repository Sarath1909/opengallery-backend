package com.streamspot.webapp.opengallary.model;

import java.io.Serializable;
import java.util.Objects;

public class AgentTokenId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long recipientId;
	private String tokenType;
	private String token;
	
	public AgentTokenId() {}

	public AgentTokenId(long recipientId, String tokenType, String token) {
		super();
		this.recipientId = recipientId;
		this.tokenType = tokenType;
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(recipientId, token, tokenType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentTokenId other = (AgentTokenId) obj;
		return recipientId == other.recipientId && Objects.equals(token, other.token)
				&& Objects.equals(tokenType, other.tokenType);
	}
	
}
