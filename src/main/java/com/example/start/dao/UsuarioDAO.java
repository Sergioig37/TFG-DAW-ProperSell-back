package com.example.start.dao;

import java.util.Optional;

import com.example.start.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAO extends CrudRepository<Usuario, Long>{

	Optional<Usuario> findByUsername(String username);

	
}

