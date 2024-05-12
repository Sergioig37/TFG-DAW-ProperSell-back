package com.spring.start.usuarios;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioDAO extends CrudRepository<Usuario, String>{

	@Query("SELECT correo FROM Usuario us where us.correo = correo")
	Optional<Usuario>  findByCorreo(String correo);
}
