package com.example.start.service;

import com.example.start.auth.AuthResponse;
import com.example.start.auth.AuthService;
import com.example.start.auth.LoginRequest;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.AlertaDTO;
import com.example.start.dto.UsuarioDTO;
import com.example.start.entity.Alerta;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {


    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    AuthService authService;

    public ResponseEntity borrarUsuario(String username) {
        Optional<Usuario> usuarioOptional = usuarioDAO.findByUsername(username);

        if (usuarioOptional.isPresent()) {
            usuarioDAO.delete(usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<AuthResponse> editarUsuario(UsuarioDTO usuarioDTO, String username) throws Exception {

        Optional<Usuario> usuarioOptional = usuarioDAO.findByUsername(username);
        if (usuarioOptional.isPresent()) {
            Usuario existingUser = usuarioOptional.get();
            existingUser.setCorreo(usuarioDTO.getCorreo());
            existingUser.setUsername(usuarioDTO.getUsername());
            existingUser.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
            if(usuarioDTO.getPassword()!=""){
                existingUser.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }
            existingUser.setNombreReal(usuarioDTO.getNombreReal());
            usuarioDAO.save(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body(this.lanzarNuevoToken(usuarioDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private AuthResponse lanzarNuevoToken(UsuarioDTO usuarioDTO) throws Exception {

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername(usuarioDTO.getUsername());
        loginRequest.setPassword(usuarioDTO.getPassword());

        return authService.login(loginRequest);

    }

    public ResponseEntity<List<Propiedad>> getPropiedadesDelUsuario(String username) {
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

    public ResponseEntity<Set<AlertaDTO>> getAlertasUsuario(@PathVariable String username){

        Optional<Usuario> usuario = usuarioDAO.findByUsername(username);

        Set<Alerta> alertas = usuario.get().getAlertas();
        System.out.println(alertas);
        System.out.println(usuario.get());
        Set<AlertaDTO> alertasDTO = new HashSet<>();
        for(Alerta alert: alertas){
            AlertaDTO alertaDTO = new AlertaDTO();

            alertaDTO.setDescripcion(alert.getDescripcion());
            alertaDTO.setNombre(alert.getNombre());
            alertasDTO.add(alertaDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(alertasDTO);

    }
}
