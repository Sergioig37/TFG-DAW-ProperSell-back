package com.example.start.controller;

import java.util.*;

import com.example.start.auth.AuthResponse;
import com.example.start.auth.AuthService;
import com.example.start.auth.LoginRequest;
import com.example.start.dao.AlertaDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.AlertaDTO;
import com.example.start.dto.UsuarioDTO;
import com.example.start.entity.Alerta;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import com.example.start.jwt.JwtService;
import com.example.start.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping("/usuario/disable/{username}")
//    public ResponseEntity deshabilitarUsuario(@PathVariable String username){
//
//        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);
//
//        if(usuario.isPresent()){
//            usuario.get().setHabilitado(false);
//            usuarioDAO.save(usuario.get());
//            return ResponseEntity.status(HttpStatus.OK).body(null);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }
//
//    @GetMapping("/usuario/enable/{username}")
//    public ResponseEntity habilitarUsuario(@PathVariable String username){
//
//        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);
//
//        if(usuario.isPresent()){
//            usuario.get().setHabilitado(true);
//            usuarioDAO.save(usuario.get());
//            return ResponseEntity.status(HttpStatus.OK).body(null);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }


    @GetMapping("/usuario/{username}/alertas")
    public ResponseEntity<Set<AlertaDTO>> getAlertasUsuario(@PathVariable String username){

       return usuarioService.getAlertasUsuario(username);

    }
}
