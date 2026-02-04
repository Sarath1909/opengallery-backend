package com.streamspot.webapp.opengallary.dto;

import java.util.Set;

import com.streamspot.webapp.opengallary.model.Role;

public class EnrollAgentRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String retypePassword;
	private Set<Role> roles;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRetypePassword() {
		return retypePassword;
	}
	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "EnrollAgentResponse [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", retypePassword=" + retypePassword + ", roles=" + roles + "]";
	}

}

