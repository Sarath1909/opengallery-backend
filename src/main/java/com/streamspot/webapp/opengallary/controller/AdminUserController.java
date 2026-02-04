package com.streamspot.webapp.opengallary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamspot.webapp.opengallary.dto.EnrollAgentRequest;
import com.streamspot.webapp.opengallary.dto.EnrollAgentResponse;
import com.streamspot.webapp.opengallary.model.AdminUser;
import com.streamspot.webapp.opengallary.security.JwtUtil;
import com.streamspot.webapp.opengallary.service.AdminUserService;
import com.streamspot.webapp.opengallary.service.AgentTokenService;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollAgent(@RequestBody EnrollAgentRequest request) {
        try {
            AdminUser createdUser = adminUserService.enrollAgent(request);

            EnrollAgentResponse response = new EnrollAgentResponse();
            response.setFullName(createdUser.getFname() + " " + createdUser.getLname());
            response.setActive(createdUser.isActive());
            response.setEmail(createdUser.getEmail());
            response.setId(createdUser.getId());   // assuming AdminUser has getId()
            response.setRoles(createdUser.getRoles());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong while enrolling user."));
        }
    }  
    
}

