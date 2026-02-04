package com.streamspot.webapp.opengallary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.streamspot.webapp.opengallary.model.AgentToken;
import com.streamspot.webapp.opengallary.model.AgentTokenId;

@Repository
public interface AgentTokenRepository extends JpaRepository<AgentToken, AgentTokenId>{

	@Query(
		    value = "SELECT AT.* " +
		            "FROM OPENGALLERY.AGENT_TOKEN AT " +
		            "WHERE AT.RECIPIENT_ID = :recipientId AND AT.TOKEN_TYPE = :tokenType AND AT.TOKEN_EXPIRY > CURRENT_TIMESTAMP " +
		            "LIMIT 1",
		    nativeQuery = true
		)
		AgentToken findActiveToken(@Param("recipientId") Long recipientId, 
		                                @Param("tokenType") String tokenType);
	
	
	@Query(
		    value = "SELECT AT.* " +
		            "FROM OPENGALLERY.AGENT_TOKEN AT " +
		            "WHERE AT.RECIPIENT_ID = :recipientId " +
		            "AND AT.TOKEN_TYPE = :tokenType " +
		            "AND AT.TOKEN_EXPIRY > CURRENT_TIMESTAMP",
		    nativeQuery = true
		)
		List<AgentToken> findAllActiveTokens(Long recipientId, String tokenType);

}
