package com.example.start.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	ADMIN,
	USER;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.name();
	}
}
