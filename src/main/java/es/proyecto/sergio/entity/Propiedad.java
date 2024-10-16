package es.proyecto.sergio.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Propiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tipo;

	private String localizacion;

	private Long precio;

	private int superficie;

	@Getter
	@Setter
	private boolean habilitado;


	@JoinColumn(name = "FK_PROPIETARIO")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	private Usuario propietario;


	@Override
	public String toString() {
		return "Propiedad{" +
				"id=" + id +
				", tipo='" + tipo + '\'' +
				", localizacion='" + localizacion + '\'' +
				", precio=" + precio +
				", habilitado=" + habilitado +
				", propietario=" + propietario.getUsername() +
				", superficie=" + superficie +
				'}';
	}
}
