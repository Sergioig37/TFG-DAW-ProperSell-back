package es.proyecto.sergio.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Rol implements GrantedAuthority{
	ADMIN,
	USER;




	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.name();
	}
}
