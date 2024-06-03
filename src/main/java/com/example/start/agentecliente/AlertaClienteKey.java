package com.example.start.agentecliente;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
public class AlertaClienteKey implements Serializable{

	@Column(name = "cliente_id")
	Long clienteId;
	
	@Column(name = "alerta_id")
	Long alertaId;

	public Long getAlertaId() {
		return alertaId;
	}

	public void setAgenteId(Long agenteId) {
		this.alertaId = agenteId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(alertaId, clienteId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlertaClienteKey other = (AlertaClienteKey) obj;
		return Objects.equals(alertaId, other.alertaId) && Objects.equals(clienteId, other.clienteId);
	}

}
