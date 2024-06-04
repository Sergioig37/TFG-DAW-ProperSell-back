package com.example.start.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlerta")
    private Long id;

    private String nombre;

    private String descripcion;

    @ManyToMany(mappedBy = "alertas", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Usuario> usuarios;


    @Override
    public String toString() {
        return "Alerta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
