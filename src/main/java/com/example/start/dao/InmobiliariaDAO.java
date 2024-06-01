package com.example.start.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.start.entity.Inmobiliaria;

import java.util.List;

@Repository
public interface InmobiliariaDAO extends CrudRepository<Inmobiliaria, Long> {

    @Query("SELECT i FROM Inmobiliaria i WHERE i.numeroEmpleados > :numeroEmpleados")
    List<Inmobiliaria> findInmobiliariasByNumeroEmpleadosMayorQue(@Param("numeroEmpleados") Long numeroEmpleados);

}
