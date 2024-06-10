package es.proyecto.sergio.controller;

import java.util.*;

import es.proyecto.sergio.service.AuthService;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.dto.AlertaDTO;
import es.proyecto.sergio.dto.UsuarioDTO;
import es.proyecto.sergio.entity.Propiedad;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsuarioService usuarioService;

    private static final Logger logger
            = LoggerFactory.getLogger(UsuarioController.class);

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body((List<Usuario>)usuarioDAO.findAll());
    }


    @GetMapping("/usuarioNombre/{username}")
    public ResponseEntity<Usuario> getUsuarioByUsername(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDAO.findByUsername(username).get());

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id){


        return usuarioService.existe(id);

    }


    @DeleteMapping("/del/{id}/{username}")
    public ResponseEntity<?> delUsuario(@PathVariable Long id,@PathVariable String username) {


        return usuarioService.borrarUsuario(id, username);

    }


    @PutMapping("/edit/{id}/{username}")
    public ResponseEntity<?> editUsername(@RequestBody @Valid UsuarioDTO usuarioDTO, @PathVariable Long id, BindingResult bindingResult, @PathVariable String username)  {

        System.out.println(username);

        return usuarioService.editarUsuario(usuarioDTO, id, bindingResult, username);
    }


   @GetMapping("/propiedades/{id}")
   public ResponseEntity<List<Propiedad>> getMisPropiedades(@PathVariable Long id){

     return  usuarioService.getPropiedadesDelUsuario(id);

   }

    @GetMapping("/usuarioInfoContacto/{id}")
    public ResponseEntity<Usuario> getNumeroDueño(@PathVariable Long id){

       return usuarioService.getNumeroPropietario(id);

    }


    @GetMapping("/usuarioExcluido/{id}")
    public ResponseEntity<List<Usuario>> getRestoUsuarios(@PathVariable Long id){

        List<Usuario> usuarios = usuarioDAO.findRestoUsuarios(id);

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);

    }


   @PutMapping("/enabled/{id}/{enabled}")
    public void habilitacionUsuario(@PathVariable Long id, @PathVariable boolean enabled){

        usuarioService.habilitacionUsuario(id, enabled);

   }


    @GetMapping("/{id}/alertas")
    public ResponseEntity<Set<AlertaDTO>> getAlertasUsuario(@PathVariable Long id){


        Set<AlertaDTO> alertas = usuarioService.getAlertasUsuario(id);

       return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }

    @GetMapping("/{id}/alertasDisponibles")
    public ResponseEntity<Set<AlertaDTO>> getAlertasDisponibles(@PathVariable Long id){

            Set<AlertaDTO> alertas = usuarioService.getAlertasDisponibles(id);

            return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }


    @PutMapping("/{idUsuario}/{id}/{add}")
    public void guardarAlerta(@PathVariable Long idUsuario, @PathVariable Long id, @PathVariable boolean add){

        usuarioService.actualizarAlerta(idUsuario, id, add);

    }
}
