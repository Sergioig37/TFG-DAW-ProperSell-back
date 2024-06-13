package es.proyecto.sergio.controller;

import es.proyecto.sergio.dao.PropiedadDAO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.dto.AlertaDTO;
import es.proyecto.sergio.dto.EstadisticasDTO;
import es.proyecto.sergio.dto.PropiedadDTO;
import es.proyecto.sergio.dto.UsuarioDTO;
import es.proyecto.sergio.entity.Alerta;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.service.AlertaService;
import es.proyecto.sergio.service.EstadisticasService;
import es.proyecto.sergio.service.PropiedadService;
import es.proyecto.sergio.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {


    @Autowired
    PropiedadDAO propiedadDAO;



    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    PropiedadService propiedadService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AlertaService alertaService;


    @Autowired
    EstadisticasService estadisticasService;

    private static final Logger logger
            = LoggerFactory.getLogger(EstadisticasController.class);

    @GetMapping("/propiedadMasCarasDe/{precio}")
    public ResponseEntity<List<PropiedadDTO>> getPropiedadesCaras(@PathVariable Long precio) {

            List<PropiedadDTO> propiedades = propiedadService.propiedadesMasCarasQue(precio);

            return ResponseEntity.status(HttpStatus.OK).body(propiedades);

    }



    @GetMapping("/usuarioConMasDe/{numeroAlertas}/alertas")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosConMasXAlertas(@PathVariable Long numeroAlertas) {

       List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosConMasDeXAlertas(numeroAlertas);


       return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping("/usuario/variasPropiedades")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosConMasDeUnaPropiedad() {

        List<Usuario> usuarios = usuarioService.findUsuarioMaDeUnaPropiedad();


        List<UsuarioDTO> usuarioDTOS = usuarioService.convertirAListaUsuarioDTO(usuarios);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDTOS);
    }

    @GetMapping("/alertas/variosUsuarios")
    public ResponseEntity<List<AlertaDTO>> getAlertasPopulares() {

        List<AlertaDTO> alertas = alertaService.encontrarAlertasPopulares();


        return ResponseEntity.status(HttpStatus.OK).body(alertas);
    }

    @GetMapping("/usuarios/baneados")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosBaneados() {

        List<Usuario> usuarios = usuarioDAO.findByHabilitado(false);

        List<UsuarioDTO> usuarioDTOS = usuarioService.convertirAListaUsuarioDTO(usuarios);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDTOS);
    }

    @GetMapping("/alertas/{descripcionSize}")
    public ResponseEntity<List<AlertaDTO>> getAlertasLargas(@PathVariable Long descripcionSize) {


        List<AlertaDTO> alertasDTO = alertaService.getAlertasMasLargas(descripcionSize);

        return ResponseEntity.status(HttpStatus.OK).body(alertasDTO);

    }


    @GetMapping("/generarPdf/{numeroAlertas}/{precio}")
    public ResponseEntity<EstadisticasDTO> getEstadisticasPdf(@PathVariable Long numeroAlertas, @PathVariable Long precio){

        List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosConMasDeXAlertas(numeroAlertas);
        List<PropiedadDTO> propiedades = propiedadService.propiedadesMasCarasQue(precio);


        EstadisticasDTO estadisticasDTO = EstadisticasDTO.builder()
                .usuarios(usuarios)
                .propiedades(propiedades)
                .numeroAlertas(numeroAlertas)
                .precio(precio)
                .build();
        try{
            byte[] contents = estadisticasService.getEstadisticasPDF(estadisticasDTO);
            estadisticasDTO.setContents(Base64.getEncoder().encodeToString(contents));
            return ResponseEntity.status(HttpStatus.OK).body(estadisticasDTO);
        }
        catch (Exception e){
            logger.error(e.getMessage() + " " + e);
            return null;
        }

    }
}
