package com.example.start.controller;

import java.util.*;

import com.example.start.auth.AuthResponse;
import com.example.start.auth.AuthService;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.AlertaDTO;
import com.example.start.dto.UsuarioDTO;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import com.example.start.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
public class UsuarioController {

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsuarioService usuarioService;


    @GetMapping("/usuario")
    public ResponseEntity<List<Usuario>> getUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body((List<Usuario>)usuarioDAO.findAll());
    }


    @GetMapping("/usuario/{username}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDAO.findByUsername(username).get());

    }


    @DeleteMapping("/usuario/del/{username}")
    public ResponseEntity delUsuario(@PathVariable String username) {


        return usuarioService.borrarUsuario(username);

    }

    @PutMapping("/usuario/edit/{username}")
    public ResponseEntity<AuthResponse> editUsername(@RequestBody @Valid UsuarioDTO usuarioDTO, @PathVariable String username) throws Exception {
        return usuarioService.editarUsuario(usuarioDTO, username);
    }

   @GetMapping("/usuario/propiedades/{username}")
   public ResponseEntity<List<Propiedad>> getMisPropiedades(@PathVariable String username){

     return  usuarioService.getPropiedadesDelUsuario(username);

   }

    @GetMapping("/usuarioInfoContacto/{username}")
    public ResponseEntity<Usuario> getNumeroDueño(@PathVariable String username){

       return usuarioService.getNumeroDueño(username);

    }

    @GetMapping("/usuarioExcluido/{username}")
    public ResponseEntity<List<Usuario>> getRestoUsuarios(@PathVariable String username){

        List<Usuario> usuarios = usuarioDAO.findRestoUsuarios(username);

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);

    }


   @PutMapping("/usuario/enabled/{username}/{enabled}")
    public void habilitarUsuario(@PathVariable String username, @PathVariable boolean enabled){

        usuarioService.habilitarDeshabilitarUsuario(username, enabled);

   }


    @GetMapping("/usuario/{username}/alertas")
    public ResponseEntity<Set<AlertaDTO>> getAlertasUsuario(@PathVariable String username){


        Set<AlertaDTO> alertas = usuarioService.getAlertasUsuario(username);

       return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }

    @GetMapping("/usuario/{username}/alertasDisponibles")
    public ResponseEntity<Set<AlertaDTO>> getAlertasDisponibles(@PathVariable String username){

            Set<AlertaDTO> alertas = usuarioService.getAlertasDisponibles(username);

            return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }


    @PutMapping("/usuario/{username}/{id}/{add}")
    public void guardarAlerta(@PathVariable String username, @PathVariable Long id, @PathVariable boolean add){

        usuarioService.actualizarAlerta(username, id, add);

    }
}
