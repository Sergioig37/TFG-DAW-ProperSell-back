package com.example.start.entity;
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
import lombok.Getter;
import lombok.Setter;


@Entity
public class Agente {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Setter
	@Getter
    private String nombre;
	@Setter
	@Getter
	private String correo;


	@JoinColumn(name = "FK_INMOBILIARIA")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference

	@Setter
	@Getter
	private Inmobiliaria inmobiliaria;
	@Setter
	@Getter
	private String numeroTelefono;
	
	@OneToMany(targetEntity=AgenteCliente.class, mappedBy = "agente", cascade = CascadeType.ALL)
	@Setter
	@Getter
	private List<AgenteCliente> agenteCliente;

}