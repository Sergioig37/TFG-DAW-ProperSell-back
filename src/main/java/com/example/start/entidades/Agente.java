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
import lombok.Data;


@Entity
@Data
public class Agente {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String correo;


	@JoinColumn(name = "FK_INMOBILIARIA")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	private Inmobiliaria inmobiliaria;
	
	private String numeroTelefono;
	
	@OneToMany(targetEntity=AgenteCliente.class, mappedBy = "agente", cascade = CascadeType.ALL)
	private List<AgenteCliente> agenteCliente;
	




	
	
	
}