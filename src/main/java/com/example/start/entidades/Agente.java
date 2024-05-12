package com.example.start.entidades;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Agente {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@JoinColumn(name = "FK_INMOBILIARIA")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	private Inmobiliaria inmobiliaria;
	
	private String numeroTelefono;
	
	@OneToMany(targetEntity=AgenteCliente.class, mappedBy = "agente", cascade = CascadeType.ALL)
	private List<AgenteCliente> agenteCliente;
	

	public List<AgenteCliente> getAgenteCliente() {
		return agenteCliente;
	}

	public void setAgenteCliente(List<AgenteCliente> agenteCliente) {
		this.agenteCliente = agenteCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public Inmobiliaria getInmobiliaria() {
		return inmobiliaria;
	}

	public void setInmobiliaria(Inmobiliaria inmobiliaria) {
		this.inmobiliaria = inmobiliaria;
	}

	@Override
	public String toString() {
		return "Agente [id=" + id + ", nombre=" + nombre + ", inmobiliaria=" + inmobiliaria + ", numeroTelefono="
				+ numeroTelefono + "]";
	}

	
	
	
	
}