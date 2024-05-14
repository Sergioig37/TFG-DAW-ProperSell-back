package com.example.start.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.start.entidades.Usuario;

@Repository
public interface UsuarioDAO extends CrudRepository<Usuario, String>{

}
