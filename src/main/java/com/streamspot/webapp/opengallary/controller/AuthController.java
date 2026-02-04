package com.streamspot.webapp.opengallary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streamspot.webapp.opengallary.dto.LoginRequest;
import com.streamspot.webapp.opengallary.dto.ResetPasswordRequest;
import com.streamspot.webapp.opengallary.model.AdminUser;
import com.streamspot.webapp.opengallary.service.AdminUserService;
import com.streamspot.webapp.opengallary.service.AgentTokenService;
import com.streamspot.webapp.opengallary.service.JwtTokenService;
import com.streamspot.webapp.opengallary.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    JwtTokenService jwtTokenService;
   
    @Autowired
    AgentTokenService agentTokenService;
    
    @Autowired
    AgentTokenService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AdminUser user = adminUserService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            List<String> roles = user.getRoles().stream()
                                     .map(role -> role.getName().name())
                                     .toList();

            String token = jwtUtil.generateToken(user.getEmail(), roles);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", e.getMessage()));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(Map.of("error", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                                 .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            System.err.println("Unexpected error in /login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Internal server error. Please try again later."));
        }
    }
    
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            boolean status = adminUserService.verifyEmailAddress(token);

            if (status) {
                return ResponseEntity.ok("Email verified successfully. Please reset your password.");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired token.");
            }

        } catch (IllegalArgumentException e) {
            // e.g. User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for this token.");
        } catch (SecurityException e) {
            // e.g. Token mismatch or expired
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired.");
        } catch (Exception e) {
            // Unexpected errors
            System.err.println("Unexpected error in verify-email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal server error. Please try again later.");
        }
    }

    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ResetPasswordRequest request) {
        try {
            boolean status = adminUserService.resetPassword(token, request);

            if (status) {
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("Invalid or expired token.");
            }

        } catch (IllegalArgumentException e) {
            // Passwords don't match, or user not found
            return ResponseEntity.badRequest().body("User or Passwords did not match." + e.getMessage());
        } catch (SecurityException e) {
            // Token validation issues
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid token." + e.getMessage());
        } catch (Exception e) {
            // Unexpected error
            System.err.println("Unexpected error in reset-password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal server error. Please try again later.");
        }
    } 
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7); // Remove "Bearer "

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid token"));
            }

            // Optionally blacklist the token until it expires
            tokenBlacklistService.blacklistToken(token);

            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));

        } catch (Exception e) {
            System.err.println("Unexpected error in /logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error. Please try again later."));
        }
    }
}
