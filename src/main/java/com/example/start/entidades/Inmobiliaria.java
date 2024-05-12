package com.example.start.entidades;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Inmobiliaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String numeroEmpleados;
	
	private String direccion;
	
	@OneToMany(mappedBy="inmobiliaria", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<Agente> agentes;

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

	public String getNumeroEmpleados() {
		return numeroEmpleados;
	}

	public void setNumeroEmpleados(String numeroEmpleados) {
		this.numeroEmpleados = numeroEmpleados;
	}

	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	

	public List<Agente> getAgentes() {
		return agentes;
	}

	public void setAgentes(List<Agente> agentes) {
		this.agentes = agentes;
	}

	@Override
	public String toString() {
		return "Inmobiliaria [id=" + id + ", nombre=" + nombre + ", numeroEmpleados=" + numeroEmpleados + "dirección " + direccion +"]";
	}
	
	
}
