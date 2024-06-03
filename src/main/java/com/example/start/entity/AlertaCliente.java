package com.example.start.entity;



import com.example.start.agentecliente.AlertaClienteKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Data
public class AlertaCliente {

	@EmbeddedId
	private AlertaClienteKey id;
	
	@ManyToOne
	@MapsId("clienteId")
	@JoinColumn(name = "cliente_id")
	private Usuario cliente;
	
	@ManyToOne
	@MapsId("alertaId")
	@JoinColumn(name = "alerta_id")
	private Alerta alerta;

	
	
	
}
