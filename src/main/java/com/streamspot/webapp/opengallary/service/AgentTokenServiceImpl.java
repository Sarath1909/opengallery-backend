package com.streamspot.webapp.opengallary.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streamspot.webapp.opengallary.model.AgentToken;
import com.streamspot.webapp.opengallary.repository.AgentTokenRepository;

import jakarta.persistence.PersistenceException;

@Service
public class AgentTokenServiceImpl implements AgentTokenService {
	
	@Autowired
	AgentTokenRepository agentTokenRepository;

	@Override
	public boolean saveToken(AgentToken agentToken) {
		boolean status=false;
		try {
			agentTokenRepository.save(agentToken);
			status=true;
		}catch(Exception e) {
			System.out.println("Exception is saving token"+ e);
		}
		return status;
	}

	@Override
	public boolean expireActiveToken(AgentToken agentToken) {
		boolean status=false;
		agentToken.setTokenExpiry(Instant.now());
		try {
			agentTokenRepository.save(agentToken);
			status=true;
		} catch (PersistenceException e) {
	        System.out.println("PersistenceException in expiring token: " + e.getMessage());
	    } catch(Exception e) {
	    	System.out.println("Exception in expiring token: " + e.getMessage());
	    }
		return status;
	}

	@Override
	public AgentToken findActiveToken(Long recipientId, String tokenType) {
	    AgentToken agentToken = null;
	    try {
	        agentToken = agentTokenRepository.findActiveToken(recipientId, tokenType);
	    } catch (Exception e) {
	        System.out.println("Exception in fetching token: " + e.getMessage());
	    }
	    return agentToken;
	}

	@Override
	public List<AgentToken> findAllActiveTokens(Long recipientId, String tokenType) {
		List<AgentToken> tokens=null;
		try {
			tokens=agentTokenRepository.findAllActiveTokens(recipientId, tokenType);
		}catch (Exception e) {
	        System.out.println("Exception in fetching token: " + e.getMessage());
	    }
		return tokens;
	}

	private final Set<String> blacklist = new HashSet<>();

	@Override
    public void blacklistToken(String token) {
        blacklist.add(token);
    }

	@Override
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

}
