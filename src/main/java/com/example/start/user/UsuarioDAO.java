package com.example.start.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UsuarioDAO extends CrudRepository<Usuario, Long>{

	@Query("SELECT u FROM Usuario u WHERE u.usuario = :user")
    Optional<Usuario> findByUser(@Param("user") String user);

	
}

