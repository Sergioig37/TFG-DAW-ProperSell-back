package com.example.start.entidades;



import com.spring.start.agentecliente.AgenteClienteKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
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

	
	
	public AgenteClienteKey getId() {
		return id;
	}

	public void setId(AgenteClienteKey id) {
		this.id = id;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "AgenteCliente [id=" + id + "]";
	}
	
	
	
}
