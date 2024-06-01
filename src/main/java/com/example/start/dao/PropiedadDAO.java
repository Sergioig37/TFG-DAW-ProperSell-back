package com.example.start.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.start.entity.Propiedad;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropiedadDAO extends CrudRepository<Propiedad, Long>{
    @Query("SELECT p FROM Propiedad p WHERE p.precio > :precio")
    List<Propiedad> findPropiedadesByPrecioMayorQue(@Param("precio") Long precio);
}
