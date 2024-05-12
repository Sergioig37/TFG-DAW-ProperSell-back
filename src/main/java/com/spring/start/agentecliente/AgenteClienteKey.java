package com.spring.start.agentecliente;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AgenteClienteKey implements Serializable{

	@Column(name = "agente_id")
	Long agenteId;
	
	@Column(name = "cliente_id")
	Long clienteId;

	public Long getAgenteId() {
		return agenteId;
	}

	public void setAgenteId(Long agenteId) {
		this.agenteId = agenteId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agenteId, clienteId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgenteClienteKey other = (AgenteClienteKey) obj;
		return Objects.equals(agenteId, other.agenteId) && Objects.equals(clienteId, other.clienteId);
	}
	
	
	
	
}
