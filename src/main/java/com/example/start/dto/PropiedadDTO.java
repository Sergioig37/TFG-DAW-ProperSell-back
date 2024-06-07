package com.example.start.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PropiedadDTO {

    private Long id;

    @NotEmpty(message = "El tipo no puede estar vacío")
    @Size(max = 255, message = "El tipo debe tener como máximo 255 caracteres")
    private String tipo;

    @NotEmpty(message = "La localización no puede estar vacía")
    @Size(max = 255, message = "La localización debe tener como máximo 255 caracteres")
    private String localizacion;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser un número positivo")
    private Long precio;

    private String propietario;
}
