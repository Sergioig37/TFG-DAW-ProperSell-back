package com.example.start.entity;

import java.util.Collection;
import java.util.List;

import com.example.start.user.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
//Para que no se pueda repetur
@Data
@Table(name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"correo"})})
public class Usuario implements UserDetails{

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;


	private String username;

	private String password;

	private String correo;

	private String nombreReal;

	private boolean habilitado;

	private String numeroTelefono;
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy="propietario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference

	private List<Propiedad> propiedades;

	@OneToMany(targetEntity=AlertaCliente.class, mappedBy = "cliente", cascade = CascadeType.ALL)

	private List<AlertaCliente> alertaCliente;


	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority((role.name())));
	}
	@Override
	public String getUsername() {

		return username;
	}
	@Override
	public boolean isAccountNonExpired() {

		return true;
	}
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}
	@Override
	public boolean isEnabled() {

		if(habilitado==true){
			return true;
		}
		else{
			return false;
		}


	}

}