package com.example.start.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.start.entidades.Usuario;


public interface UsuarioDAO extends CrudRepository<Usuario, String>{

}

