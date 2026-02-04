package com.streamspot.webapp.opengallary.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

@Entity
@Table(name = "audit_log", indexes = @Index(name = "idx_audit_created_at", columnList = "creation_time"))
public class AuditLog {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="audit_id", nullable=false)
  private Long id;

  @Column(name="action", nullable = false, length = 64)
  private String action; // CREATE_MEDIA, UPDATE_MEDIA, DELETE_MEDIA, LOGIN, etc.

  @Column(name="details", length = 1000)
  private String detailsJson;

  @Column(name = "admin_user_id", nullable = false)
  private Long adminUserId;

  @Column(name="creation_time", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getDetailsJson() {
		return detailsJson;
	}
	
	public void setDetailsJson(String detailsJson) {
		this.detailsJson = detailsJson;
	}
	
	public Long getAdminUserId() {
		return adminUserId;
	}
	
	public void setAdminUserId(Long adminUserId) {
		this.adminUserId = adminUserId;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	  
  
}

