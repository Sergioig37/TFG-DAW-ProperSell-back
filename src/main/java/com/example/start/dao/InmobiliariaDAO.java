package com.example.start.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.start.entity.Inmobiliaria;

@Repository
public interface InmobiliariaDAO extends CrudRepository<Inmobiliaria, Long> {

}
