package es.proyecto.sergio.dto;

import es.proyecto.sergio.entity.Usuario;
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
public class EstadisticasFlex implements Serializable {

    private int precio;

    private int numeroAlertas;

    private int numeroCaracteres;



    private List<PropiedadDTO> propiedades;

    private List<UsuarioDTO> usuarios;

    private List<AlertaDTO> alertas;

}
