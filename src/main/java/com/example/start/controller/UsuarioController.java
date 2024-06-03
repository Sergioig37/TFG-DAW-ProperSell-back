package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import com.example.start.auth.AuthResponse;
import com.example.start.auth.AuthService;
import com.example.start.auth.LoginRequest;
import com.example.start.dao.UsuarioDAO;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import com.example.start.jwt.JwtService;
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
    JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/usuario")
    public ResponseEntity<List<Usuario>> getUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body((List<Usuario>)usuarioDAO.findAll());
    }


    @GetMapping("/usuario/{username}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDAO.findByUsername(username).get());

    }


    @DeleteMapping("/usuario/del/{username}")
    public ResponseEntity<Usuario> delUsuario(@PathVariable String username) {


        Optional<Usuario>  usuarioOptional = usuarioDAO.findByUsername(username);

        if (usuarioOptional.isPresent()) {
            usuarioDAO.delete(usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


    }

    @PutMapping("/usuario/edit/{username}")
    public ResponseEntity<AuthResponse> editUsername(@RequestBody @Valid Usuario usuario, @PathVariable String username) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioDAO.findByUsername(username);
        if (usuarioOptional.isPresent()) {
            Usuario existingUser = usuarioOptional.get();
            existingUser.setCorreo(usuario.getCorreo());
            existingUser.setUsername(usuario.getUsername());
            if(usuario.getPassword()!=""){
                existingUser.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            existingUser.setNombreReal(usuario.getNombreReal());
            usuarioDAO.save(existingUser);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(usuario.getUsername());
            loginRequest.setPassword(usuario.getPassword());


             return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

   @GetMapping("/usuario/propiedades/{username}")
   public ResponseEntity<List<Propiedad>> getMisPropiedades(@PathVariable String username){

        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);
        List<Propiedad> propiedades;
        if(usuario.isPresent()){
            propiedades = usuario.get().getPropiedades();
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

   }

    @GetMapping("/usuarioInfoContacto/{username}")
    public ResponseEntity<Usuario> getNumeroDueño(@PathVariable String username){

        Optional<Usuario> usuarioOptional = usuarioDAO.findByUsername(username);

        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

    }

    @GetMapping("/usuarioExcluido/{username}")
    public ResponseEntity<List<Usuario>> getRestoUsuarios(@PathVariable String username){

        List<Usuario> usuarios = usuarioDAO.findRestoUsuarios(username);

        return ResponseEntity.status(HttpStatus.OK).body(usuarios);

    }

    @GetMapping("/usuario/disable/{username}")
    public ResponseEntity deshabilitarUsuario(@PathVariable String username){

        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);

        if(usuario.isPresent()){
            usuario.get().setHabilitado(false);
            usuarioDAO.save(usuario.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/usuario/enable/{username}")
    public ResponseEntity habilitarUsuario(@PathVariable String username){

        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);

        if(usuario.isPresent()){
            usuario.get().setHabilitado(true);
            usuarioDAO.save(usuario.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
