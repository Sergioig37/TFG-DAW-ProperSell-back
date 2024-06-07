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
import org.springframework.validation.BindingResult;
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


    @GetMapping("/usuarioNombre/{username}")
    public ResponseEntity<Usuario> getUsuarioByUsername(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDAO.findByUsername(username).get());

    }
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDAO.findById(id).get());

    }


    @DeleteMapping("/usuario/del/{id}")
    public ResponseEntity delUsuario(@PathVariable Long id) {


        return usuarioService.borrarUsuario(id);

    }

    @PutMapping("/usuario/edit/{id}")
    public ResponseEntity<?> editUsername(@RequestBody @Valid UsuarioDTO usuarioDTO, @PathVariable Long id, BindingResult bindingResult)  {


        return usuarioService.editarUsuario(usuarioDTO, id, bindingResult);
    }

   @GetMapping("/usuario/propiedades/{id}")
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


   @PutMapping("/usuario/enabled/{id}/{enabled}")
    public void habilitarUsuario(@PathVariable Long id, @PathVariable boolean enabled){

        usuarioService.habilitarDeshabilitarUsuario(id, enabled);

   }


    @GetMapping("/usuario/{id}/alertas")
    public ResponseEntity<Set<AlertaDTO>> getAlertasUsuario(@PathVariable Long id){


        Set<AlertaDTO> alertas = usuarioService.getAlertasUsuario(id);

       return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }

    @GetMapping("/usuario/{id}/alertasDisponibles")
    public ResponseEntity<Set<AlertaDTO>> getAlertasDisponibles(@PathVariable Long id){

            Set<AlertaDTO> alertas = usuarioService.getAlertasDisponibles(id);

            return ResponseEntity.status(HttpStatus.OK).body(alertas);

    }


    @PutMapping("/usuario/{idUsuario}/{id}/{add}")
    public void guardarAlerta(@PathVariable Long idUsuario, @PathVariable Long id, @PathVariable boolean add){

        usuarioService.actualizarAlerta(idUsuario, id, add);

    }
}
