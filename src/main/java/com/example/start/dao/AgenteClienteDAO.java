package com.example.start.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.start.entidades.AgenteCliente;
import com.spring.start.agentecliente.AgenteClienteKey;



public interface AgenteClienteDAO extends CrudRepository<AgenteCliente, AgenteClienteKey>{

	@Query(value = "SELECT * FROM agente_cliente WHERE cliente_id = :cliente AND agente_id = :agente", nativeQuery = true)
	Optional<AgenteCliente> getAgenteClienteIgual(@Param("cliente") Long cliente, @Param("agente") Long agente);
}
