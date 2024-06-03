package com.example.start.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Propiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tipo;

	private String localizacion;

	private Long precio;

	@JoinColumn(name = "FK_PROPIETARIO")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	private Usuario propietario;


	
	
}
