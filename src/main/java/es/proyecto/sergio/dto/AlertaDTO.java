package es.proyecto.sergio.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlertaDTO {

    private String id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max = 255, min = 4, message = "El nombre debe tener como máximo 255 caracteres")
    private String nombre;

    @NotEmpty(message = "La descripción no puede estar vacía")
    @Size(max = 255, min = 4, message = "La descripción debe tener como máximo 255 caracteres")
    private String descripcion;

    private String numeroUsuarios;
}
