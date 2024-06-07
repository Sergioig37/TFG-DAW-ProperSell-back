package com.example.start.controller;

import com.example.start.dao.PropiedadDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.AlertaDTO;
import com.example.start.entity.Alerta;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import com.example.start.service.AlertaService;
import com.example.start.service.PropiedadService;
import com.example.start.service.UsuarioService;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/propiedadMasCarasDe/{precio}")
    public ResponseEntity<List<Propiedad>> getNumeroPropiedades(@PathVariable Long precio) {

            List<Propiedad> propiedades = propiedadService.propiedadesMasCarasQue(precio);

            return ResponseEntity.status(HttpStatus.OK).body(propiedades);

    }



    @GetMapping("/usuarioConMasDe/{numeroAlertas}/alertas")
    public ResponseEntity<List<Usuario>> getUsuariosConXAlertas(@PathVariable Long numeroAlertas) {

       List<Usuario> usuarios = usuarioService.buscarUsuariosConMasDeXAlertas(numeroAlertas);


       return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping("/usuario/variasPropiedades")
    public ResponseEntity<List<Usuario>> getUsuariosConMasDeUnaPropiedad() {

        List<Usuario> usuarios = usuarioService.findUsuarioMaDeUnaPropiedad();


        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping("/alertas/variosUsuarios")
    public ResponseEntity<List<AlertaDTO>> getAlertasPopulares() {

        List<AlertaDTO> alertas = alertaService.encontrarAlertasPopulars();


        return ResponseEntity.status(HttpStatus.OK).body(alertas);
    }

    @GetMapping("/usuarios/baneados")
    public ResponseEntity<List<Usuario>> getUsuariosBaneados() {

        List<Usuario> usuarios = usuarioDAO.findByHabilitado(false);


        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @GetMapping("/alertas/{descripcionSize}")
    ResponseEntity<List<Alerta>> getAlertasLargas(@PathVariable Long descripcionSize) {

        List<Alerta> alertas = alertaService.getAlertasMasLargas(descripcionSize);


        return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }

}
