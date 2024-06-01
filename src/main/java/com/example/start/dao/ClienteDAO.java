package com.example.start.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.start.entity.Cliente;

import java.util.List;

public interface ClienteDAO extends CrudRepository<Cliente, Long>{

    @Query("SELECT c FROM Cliente c WHERE SIZE(c.propiedades) > 1")
    List<Cliente> findClientesConMasDeUnaPropiedad();

    @Query("SELECT c FROM Cliente c WHERE c.id IN (SELECT ac.cliente.id FROM AgenteCliente ac GROUP BY ac.agente.id HAVING COUNT(ac.cliente.id) > 1)")
    List<Cliente> findClientesQueCompartenAgente();
}
