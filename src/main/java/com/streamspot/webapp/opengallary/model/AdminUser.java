package com.streamspot.webapp.opengallary.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@Table(name="admin_user", schema="opengallery",
		uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AdminUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id", nullable=false)
	private Long id;
	
	@Column(name="fname", nullable=false)
	private String fname;
	
	@Column(name="lname", nullable=false)
	private String lname;
	
	@Column(name="email", nullable=false, unique=true, length=190)
	private String email;
	
	@Column(name="password", nullable=false)
	private String passwordHash;
	
	@Column(name="isactive", nullable=false)
	private boolean active=true;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "admin_user_roles",
        joinColumns = @JoinColumn(name = "admin_user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
	
	@Column(name="creation_time", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();
	
	@Column(name="validate_email", nullable = false)
	private boolean isEmailValid;
	
	@Column(name="updated_time", nullable = false)
	private Instant updatedAt;

	@Column(name = "last_login")
	private Instant lastLoginAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(Instant lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public boolean isEmailValid() {
		return isEmailValid;
	}

	public void setEmailValid(boolean isEmailValid) {
		this.isEmailValid = isEmailValid;
	}

	public Instant isUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
