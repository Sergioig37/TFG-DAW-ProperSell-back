package com.example.start.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @OneToMany(targetEntity=AlertaCliente.class, mappedBy = "alerta", cascade = CascadeType.ALL)
    private List<AlertaCliente> alertaCliente;


}
