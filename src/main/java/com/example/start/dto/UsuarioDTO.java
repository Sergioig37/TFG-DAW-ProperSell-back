package com.example.start.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El nombre real no puede estar vacío")
    private String nombreReal;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Size(min = 9, max = 9, message = "El número de teléfono debe tener 9 dígitos")
    @Pattern(regexp = "\\d+", message = "El número de teléfono debe contener solo números")
    private String numeroTelefono;


    private boolean habilitado;
}
