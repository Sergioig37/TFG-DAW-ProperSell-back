package com.example.start.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.start.agentecliente.AlertaClienteKey;
import com.example.start.entity.AlertaCliente;



public interface AlertaClienteDAO extends CrudRepository<AlertaCliente, AlertaClienteKey>{

//	@Query(value = "SELECT * FROM agente_cliente WHERE cliente_id = :cliente AND agente_id = :agente", nativeQuery = true)
//	Optional<AlertaCliente> getAgenteClienteIgual(@Param("cliente") Long cliente, @Param("agente") Long agente);
//
//
//
//	@Query("SELECT c FROM Cliente c WHERE c.id NOT IN (SELECT ac.cliente.id FROM AgenteCliente ac WHERE ac.agente.id = :agenteId)")
//	List<Cliente> findClientesNotWorkingWithAgente(@Param("agenteId") Long agenteId);

}
