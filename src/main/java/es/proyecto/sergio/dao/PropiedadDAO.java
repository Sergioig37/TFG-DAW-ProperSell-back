package es.proyecto.sergio.dao;

import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.entity.Propiedad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropiedadDAO extends CrudRepository<Propiedad, Long>{

    @Query("SELECT p FROM Propiedad p WHERE p.precio > :precio")
    List<Propiedad> findPropiedadesByPrecioMayorQue(@Param("precio") Long precio);

    @Query("SELECT p.propietario FROM Propiedad p WHERE p.id = :propiedadId")
    Optional<Usuario> findPropietarioByPropiedadId(@Param("propiedadId") Long propiedadId);


    @Query("SELECT p FROM Propiedad p WHERE p.propietario.id != :propietarioId AND p.habilitado = true")
    List<Propiedad> findPropiedadesQueNoSonDelPropietario(@Param("propietarioId") Long propietarioId);

    @Query("SELECT p FROM Propiedad p WHERE p.habilitado = true")
    List<Propiedad> findPropiedadesHabilitadas();
}
