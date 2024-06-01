package com.example.start.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.start.entity.Agente;

import java.util.List;

@Repository
public interface AgenteDAO extends CrudRepository<Agente, Long>{

    @Query("SELECT DISTINCT a FROM Agente a JOIN a.agenteCliente ac WHERE ac.cliente IS NOT NULL")
    List<Agente> findAgentesConClientes();

    @Query("SELECT a FROM Agente a WHERE (SELECT COUNT(ac) FROM AgenteCliente ac WHERE ac.agente = a) > 1")
    List<Agente> findAgentesConMasDeUnCliente();
}
