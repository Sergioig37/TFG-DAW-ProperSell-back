package com.example.start.dto;

import com.example.start.entity.AgenteCliente;
import com.example.start.entity.Inmobiliaria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class AgenteDTO {
    private String nombre;
    private String correo;
    private String inmobiliaria;
    private String numeroTelefono;
}
