package com.example.start.controller;

import java.util.*;

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
import org.springframework.security.access.prepost.PreAuthorize;
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
