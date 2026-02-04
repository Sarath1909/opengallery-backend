package com.streamspot.webapp.opengallary.service;

import com.streamspot.webapp.opengallary.dto.EnrollAgentRequest;
import com.streamspot.webapp.opengallary.dto.ResetPasswordRequest;
import com.streamspot.webapp.opengallary.model.AdminUser;

public interface AdminUserService {
	
	public AdminUser enrollAgent(EnrollAgentRequest request);
	public AdminUser authenticate(String email, String password) throws RuntimeException;
	public AdminUser updateAgent(EnrollAgentRequest request);
	public AdminUser getAgent();
	public AdminUser findAgentByEmail(String email);
	public boolean resetPassword(String token, ResetPasswordRequest request);
	public boolean verifyEmailAddress(String token);
	
}
