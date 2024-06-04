package com.example.start.dao;

import java.util.List;
import java.util.Optional;


import com.example.start.entity.Usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UsuarioDAO extends CrudRepository<Usuario, Long>{

	Optional<Usuario> findByUsername(String username);

//	@Query("SELECT u FROM Usuario u WHERE SIZE(u.propiedades) > 1")
//	List<Usuario> findClientesConMasDeUnaPropiedad();
//
//	//@Query("SELECT u FROM Usuario  u WHERE u.id IN (SELECT ac.cliente.id FROM AgenteCliente ac GROUP BY ac.agente.id HAVING COUNT(ac.cliente.id) > 1)")
//	//List<Usuario> findClientesQueCompartenAgente();
//
//	@Query("SELECT u FROM Usuario u JOIN u.propiedades p WHERE p.id = :propiedadId")
//	Usuario findClienteByPropiedadId(@Param("propiedadId") Long propiedadId);

	@Query("SELECT u FROM Usuario u WHERE u.username != :username")
	List<Usuario> findRestoUsuarios(@Param("username") String username);


}
	


