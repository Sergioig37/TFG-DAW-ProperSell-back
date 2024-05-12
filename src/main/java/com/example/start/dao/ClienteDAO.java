package com.example.start.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.start.entidades.Cliente;

public interface ClienteDAO extends CrudRepository<Cliente, Long>{

}
