package com.streamspot.webapp.opengallary.dto;

import java.time.Instant;
import java.util.Set;

import com.streamspot.webapp.opengallary.model.Role;

public class EnrollAgentResponse {
	private Long id;
	private String fullName;
	private String email;
	private Set<Role> roles;
	private boolean isActive;
	private Instant lastLoginAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Instant getLastLoginAt() {
		return lastLoginAt;
	}
	public void setLastLoginAt(Instant lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "EnrollAgentResponse [id=" + id + ", fullName=" + fullName + ", email=" + email + ", roles=" + roles
				+ ", isActive=" + isActive + ", lastLoginAt=" + lastLoginAt + "]";
	}
	
}
