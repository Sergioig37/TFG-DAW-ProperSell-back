package es.proyecto.sergio.dao;

import java.util.List;
import java.util.Optional;


import es.proyecto.sergio.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByUsername(String username);

	@Query("SELECT u FROM Usuario u WHERE u.id != :id")
	List<Usuario> findRestoUsuarios(@Param("id") Long id);

	List<Usuario> findByHabilitado(boolean habilitado);

	Optional<Usuario> findByCorreo(String correo);

}
	


