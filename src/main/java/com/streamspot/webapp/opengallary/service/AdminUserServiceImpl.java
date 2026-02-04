package com.streamspot.webapp.opengallary.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.streamspot.webapp.opengallary.dto.EnrollAgentRequest;
import com.streamspot.webapp.opengallary.dto.ResetPasswordRequest;
import com.streamspot.webapp.opengallary.exception.PasswordResetRequiredException;
import com.streamspot.webapp.opengallary.model.AdminUser;
import com.streamspot.webapp.opengallary.model.AgentToken;
import com.streamspot.webapp.opengallary.repository.AdminUserRepository;
import com.streamspot.webapp.opengallary.Constants;

@Service
public class AdminUserServiceImpl implements AdminUserService{

	@Autowired
    AdminUserRepository adminUserRepository;
	@Autowired
    PasswordEncoder passwordEncoder;
	@Autowired
	JwtTokenService jwtTokenService;
	@Autowired
	AgentTokenService agentTokenService;
	@Autowired
	EmailService emailService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Autowired
    public AdminUserServiceImpl(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminUser enrollAgent(EnrollAgentRequest request) {
        if (!request.getPassword().equals(request.getRetypePassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (adminUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        AdminUser user = new AdminUser();
        user.setFname(request.getFirstName());
        user.setLname(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles());
        user.setActive(true);
        user.setEmailValid(false);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        try {
            return adminUserRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Invalid agent data. Possible duplicate or constraint violation.", e);
        } catch (Exception e) {
            throw new RuntimeException("Enrollment failed. Please try again later.", e);
        }
    }

    
    @Override
    public AdminUser authenticate(String email, String password) {
        AdminUser user = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        String activityType = null;

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!user.isEmailValid()) {
            handleEmailVerification(user);
        }
        if (user.getLastLoginAt() == null) {
        	activityType = Constants.ACTIVITY_TYPE_AUTHENTICATE; //Authenticate
            handlePasswordReset(user, activityType);
        }
        user.setLastLoginAt(Instant.now());
        adminUserRepository.save(user);

        return user;
    }

    private void handleEmailVerification(AdminUser user) {
        try {
            // Expire old email verification tokens
            List<AgentToken> tokens = agentTokenService.findAllActiveTokens(user.getId(), "E");
            for (AgentToken tkn : tokens) {
                agentTokenService.expireActiveToken(tkn);
            }

            // Generate new token
            String token = jwtTokenService.generateToken(user.getEmail());

            AgentToken agentToken = new AgentToken();
            agentToken.setRecipientId(user.getId());
            agentToken.setToken(token);
            agentToken.setTokenExpiry(Instant.now().plusSeconds(3600)); // 1 hr validity
            agentToken.setTokenType("E");
            agentToken.setCreatedTime(Instant.now());
            agentTokenService.saveToken(agentToken);

            // Send verification email
            String link = "http://localhost:5173/verify-email?token=" + token;
            emailService.sendEmail(
                user.getEmail(),
                "Verify Your Account",
                "Click here to verify your account:\n" + link
            );

            // Stop login flow
            throw new SecurityException("Email verification required. Verification link sent to your registered email.");
            
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            LOGGER.error("Error handling email verification for user {} : {}", user.getId(), e.getMessage());
            throw new RuntimeException("Failed to initiate email verification. Please try again later.");
        }
    }
    
    private void sentEmail(AdminUser user, String activityType, String token) {
    	try {
    		String resetLink = null;
	    	if(activityType.equalsIgnoreCase(Constants.ACTIVITY_TYPE_AUTHENTICATE)) {
				// Send reset password email
			    resetLink = "http://localhost:5173/reset-password?token=" + token;
			    emailService.sendEmail(
			        user.getEmail(),
			        Constants.EMAIL_SUBJECT_PASSWORD_RESET,
			        "Hello "+user.getFname()+" "+user.getLname()+",\r\n"
			        + "Welcome! Since this is your first login, please reset your password here:\n" 
			        + resetLink +"\r\n"
			        + "Thank you,\r\n"
			        + "StreamSpot Support Team"
			    );
	    	} else if(activityType.equalsIgnoreCase(Constants.ACTIVITY_TYPE_PASSWORD_RESET)) {
	    		resetLink = "http://localhost:5173/forget-password?token=" + token;
			    emailService.sendEmail(
			        user.getEmail(),
			        Constants.EMAIL_SUBJECT_PASSWORD_RESET,
			        "Hello "+user.getFname()+" "+user.getLname()+",\r\n"
			        + "\r\n"
			        + "We received a request to reset the password for your account. If you made this request, please click the link below to set a new password:\r\n"
			        + "\r\n"
			        + "👉 "+resetLink+"\r\n"
			        + "\r\n"
			        + "For security reasons, this link will expire in 1 hour.\r\n"
			        + "\r\n"
			        + "If you did not request a password reset, please ignore this email. Your account will remain secure.\r\n"
			        + "\r\n"
			        + "Thank you,\r\n"
			        + "StreamSpot Support Team"
			    );
	    	}
    	} catch (Exception e) {
            throw new RuntimeException("Failed to initiate email. Please try again later.", e);
        }
    }

    private String handlePasswordReset(AdminUser user, String activityType) {
        try {
            // Expire old reset tokens
            List<AgentToken> tokens = agentTokenService.findAllActiveTokens(user.getId(), Constants.TOKEN_TYPE_PASSWORD_RESET);
            for (AgentToken tkn : tokens) {
                agentTokenService.expireActiveToken(tkn);
            }

            // Generate new reset token
            String token = jwtTokenService.generateToken(user.getEmail());

            AgentToken resetAgentToken = new AgentToken();
            resetAgentToken.setRecipientId(user.getId());
            resetAgentToken.setToken(token);
            resetAgentToken.setTokenExpiry(Instant.now().plusSeconds(3600)); // 1 hr validity
            resetAgentToken.setTokenType("P");
            resetAgentToken.setCreatedTime(Instant.now());
            agentTokenService.saveToken(resetAgentToken);

            sentEmail(user, activityType, token);
            	
            // Stop login flow
            throw new PasswordResetRequiredException("Please reset your password. Reset password link sent to your registered email.");

        } catch (Exception e) {
            LOGGER.error("Error handling password reset for user {}: {}", user.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to initiate password reset. Please try again later.", e);
        }
    }


	@Override
	public AdminUser updateAgent(EnrollAgentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminUser getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminUser findAgentByEmail(String email) {
		AdminUser user = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
		return user;
	}

	@Override
	public boolean resetPassword(String token, ResetPasswordRequest request) {
	    try {
	        //Validate token and extract email
	        String email = jwtTokenService.validateAndExtractEmail(token);

	        //Find user
	        AdminUser user = findAgentByEmail(email);
	        if (user == null) {
	            throw new IllegalArgumentException("User not found for email: " + email);
	        }

	        //Validate active token
	        AgentToken agentToken = agentTokenService.findActiveToken(user.getId(), "P");
	        if (agentToken == null) {
	            throw new SecurityException("No active reset token found for user: " + user.getId());
	        }

	        if (!token.equals(agentToken.getToken())) {
	            throw new SecurityException("Reset token mismatch for user: " + user.getId());
	        }

	        if (agentToken.getTokenExpiry().isBefore(Instant.now())) {
	            throw new SecurityException("Reset token expired for user: " + user.getId());
	        }

	        //Validate passwords
	        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
	            throw new IllegalArgumentException("Passwords do not match");
	        }

	        //Update user password
	        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
	        user.setUpdatedAt(Instant.now());
	        user.setLastLoginAt(Instant.now());

	        // Expire the token (so it can't be reused)
	        agentTokenService.expireActiveToken(agentToken);
	        adminUserRepository.save(user);

	        return true;

	    } catch (IllegalArgumentException | SecurityException e) {
	    	LOGGER.error("Password reset failed: {}", e.getMessage());
	        return false;
	    } catch (Exception e) {
	        LOGGER.error("Unexpected error in resetPassword: {}",e.getMessage());
	        return false;
	    }
	}


	@Override
	public boolean verifyEmailAddress(String token) {
	    try {
	        //Validate token and extract email
	        String email = jwtTokenService.validateAndExtractEmail(token);
	        //Find user by email
	        AdminUser user = findAgentByEmail(email);
	        if (user == null) {
	            throw new IllegalArgumentException("User not found for email: " + email);
	        }
	        //Find active token for the user
	        AgentToken agentToken = agentTokenService.findActiveToken(user.getId(), "E");
	        if (agentToken == null) {
	            throw new IllegalStateException("No active token found for user: " + user.getId());
	        }
	        //Validate token
	        if (!token.equals(agentToken.getToken())) {
	            throw new SecurityException("Token mismatch for user: " + user.getId());
	        }
	        if (agentToken.getTokenExpiry().isBefore(Instant.now())) {
	            throw new SecurityException("Token expired for user: " + user.getId());
	        }
	        user.setEmailValid(true);
	        user.setUpdatedAt(Instant.now());

	        // Expire the token
	        try {
	            agentTokenService.expireActiveToken(agentToken);
	        } catch (Exception e) {
	            LOGGER.error("Warning: Failed to expire token for user {} : {} ", user.getId(), e.getMessage());
	        }
	        return true;
	    }catch (IllegalArgumentException | SecurityException e) {
	        LOGGER.error("Email verification failed: {}", e.getMessage());
	        return false;
	    }catch (Exception e) {
	    	LOGGER.error("Unexpected error in Email verification: {}", e.getMessage());
	        return false;
	    }
	}
}
