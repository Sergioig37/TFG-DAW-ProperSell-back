package es.proyecto.sergio.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EstadisticasDTO implements Serializable {

    private Long precio;

    private Long numeroAlertas;


    private List<PropiedadDTO> propiedades;

    private List<UsuarioDTO> usuarios;

    private String contents;


}