package com.example.start.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.start.entidades.Agente;

@Repository
public interface AgenteDAO extends CrudRepository<Agente, Long>{

}
