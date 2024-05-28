package com.example.start.entity;



import com.example.start.agentecliente.AgenteClienteKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Data
public class AgenteCliente {

	@EmbeddedId
	private AgenteClienteKey id;
	
	@ManyToOne
	@MapsId("agenteId")
	@JoinColumn(name = "agente_id")
	private Agente agente;
	
	@ManyToOne
	@MapsId("clienteId")
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	
	
	
}
