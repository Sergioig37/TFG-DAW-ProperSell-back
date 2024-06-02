package com.example.start.dto;

import com.example.start.entity.Cliente;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class PropiedadDTO {

    private Long id;

    private String tipo;

    private String localizacion;

    private String precio;


    private String propietario;
}
