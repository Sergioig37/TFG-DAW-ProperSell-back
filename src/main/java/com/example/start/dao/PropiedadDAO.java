package com.example.start.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.start.entity.Propiedad;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropiedadDAO extends CrudRepository<Propiedad, Long>{

    @Query("SELECT p FROM Propiedad p WHERE p.precio > :precio")
    List<Propiedad> findPropiedadesByPrecioMayorQue(@Param("precio") Long precio);

    @Query("SELECT p FROM Propiedad p WHERE p.localizacion = :localizacion")
    List<Propiedad> findByLocalizacion(@Param("localizacion") String localizacion);

    @Query("SELECT p FROM Propiedad p WHERE p.localizacion = :localizacion AND (p.precio >= :precioMin) AND (p.precio <= :precioMax)")
    List<Propiedad> findByLocalizacionAndPrecioRange(@Param("localizacion") String localizacion, @Param("precioMin") Long precioMin, @Param("precioMax") Long precioMax);

    @Query("SELECT p FROM Propiedad p WHERE (p.precio >= :precioMin) AND (p.precio <= :precioMax)")
    List<Propiedad> findByPrecioRange( @Param("precioMin") Long precioMin, @Param("precioMax") Long precioMax);

    @Query("SELECT p FROM Propiedad p WHERE p.localizacion = :localizacion AND (p.precio >= :precioMin)")
    List<Propiedad> findByLocalizacionAndPrecioMin(@Param("localizacion") String localizacion, @Param("precioMin") Long precioMin);

    @Query("SELECT p FROM Propiedad p WHERE p.localizacion = :localizacion AND (p.precio <= :precioMax)")
    List<Propiedad> findByLocalizacionAndPrecioMax(@Param("localizacion") String localizacion, @Param("precioMax") Long precioMax);

    @Query("SELECT p FROM Propiedad p WHERE (p.precio >= :precioMin)")
    List<Propiedad> findByPrecioMin(@Param("precioMin") Long precioMin);

    @Query("SELECT p FROM Propiedad p WHERE (p.precio <= :precioMax)")
    List<Propiedad> findByPrecioMax(@Param("precioMax") Long precioMax);


}
