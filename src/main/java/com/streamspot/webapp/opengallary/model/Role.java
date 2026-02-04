package com.streamspot.webapp.opengallary.model;

import com.streamspot.webapp.opengallary.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles", schema="opengallery")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id", nullable=false)
    private Long id;

    @Enumerated(EnumType.STRING) // stores "ADMIN", "MODERATOR" etc.
    @Column(name="name", nullable = false, unique = true, length = 50)
    private RoleType name;

    @Column(name="description", length = 255)
    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleType getName() {
		return name;
	}

	public void setName(RoleType name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
}
