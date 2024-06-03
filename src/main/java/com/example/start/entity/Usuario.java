package com.example.start.entity;

import java.util.Collection;
import java.util.List;

import com.example.start.user.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
//Para que no se pueda repetur
@Table(name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"correo"})})
public class Usuario implements UserDetails{

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;

	@Setter
	private String username;
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	private String correo;
	@Getter
	@Setter
	private String nombreReal;
	@Getter
	@Setter
	private String numeroTelefono;
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy="propietario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	@Getter
	@Setter
	private List<Propiedad> propiedades;

	@OneToMany(targetEntity=AlertaCliente.class, mappedBy = "cliente", cascade = CascadeType.ALL)
	@Getter
	@Setter
	private List<AlertaCliente> alertaCliente;


	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority((role.name())));
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}