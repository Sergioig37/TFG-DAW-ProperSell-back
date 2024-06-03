package com.example.start.dao;

import java.util.List;
import java.util.Optional;

import com.example.start.entity.Agente;
import com.example.start.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.start.agentecliente.AgenteClienteKey;
import com.example.start.entity.AgenteCliente;



public interface AgenteClienteDAO extends CrudRepository<AgenteCliente, AgenteClienteKey>{

	@Query(value = "SELECT * FROM agente_cliente WHERE cliente_id = :cliente AND agente_id = :agente", nativeQuery = true)
	Optional<AgenteCliente> getAgenteClienteIgual(@Param("cliente") Long cliente, @Param("agente") Long agente);

	@Query("SELECT ac.agente FROM AgenteCliente ac WHERE ac.cliente.id = :clienteId")
	List<Agente> findAgentesByClienteId(@Param("clienteId") Long clienteId);

	@Query("SELECT ac.cliente FROM AgenteCliente ac WHERE ac.agente.id = :agenteId")
	List<Cliente> findClientesByAgenteId(@Param("agenteId") Long agenteId);

	@Query("SELECT c FROM Cliente c WHERE c.id NOT IN (SELECT ac.cliente.id FROM AgenteCliente ac WHERE ac.agente.id = :agenteId)")
	List<Cliente> findClientesNotWorkingWithAgente(@Param("agenteId") Long agenteId);

}
