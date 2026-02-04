package com.streamspot.webapp.opengallary.service;

import java.util.List;

import com.streamspot.webapp.opengallary.model.AgentToken;

public interface AgentTokenService {
	public boolean saveToken(AgentToken agentToken);
	public boolean expireActiveToken(AgentToken agentToken);
	public AgentToken findActiveToken(Long recipientId, String tokenType);
	public List<AgentToken> findAllActiveTokens(Long recipientId, String tokenType);
	public void blacklistToken(String token);
    public boolean isTokenBlacklisted(String token);
}
