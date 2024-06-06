package com.example.start.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class UsuarioService {


    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AlertaDAO alertaDAO;

    @Autowired
    AuthService authService;

    public ResponseEntity borrarUsuario(Long id) {
        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);

        if (usuarioOptional.isPresent()) {
            usuarioDAO.delete(usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<AuthResponse> editarUsuario(UsuarioDTO usuarioDTO, Long id) throws Exception {

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario existingUser = usuarioOptional.get();
            existingUser.setCorreo(usuarioDTO.getCorreo());
            existingUser.setUsername(usuarioDTO.getUsername());
            existingUser.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
            if (usuarioDTO.getPassword() != "") {
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

    public ResponseEntity<List<Propiedad>> getPropiedadesDelUsuario(Long id) {
        Optional<Usuario> usuario = usuarioDAO.findById(id);
        List<Propiedad> propiedades;
        if (usuario.isPresent()) {
            propiedades = usuario.get().getPropiedades();
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<Usuario> getNumeroPropietario(@PathVariable Long id) {

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

    }

    public Set<AlertaDTO> getAlertasUsuario(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        Set<Alerta> alertas = usuario.get().getAlertas();


        Set<AlertaDTO> alertasUsuarioDTO = this.crearAlertasDTO(alertas);


        return alertasUsuarioDTO;

    }

    public Set<AlertaDTO> getAlertasDisponibles(Long id) {

        List<Alerta> alertasTotalesList = (List<Alerta>) alertaDAO.findAll();

        Set<Alerta> alertasTotales = this.convertirListASet(alertasTotalesList);

        Set<Alerta> alertasRestantes = this.comprobarAlertas(alertasTotales, id);

        Set<AlertaDTO> alertasRestantesDTO = this.crearAlertasDTO(alertasRestantes);

        return alertasRestantesDTO;
    }


    private Set<Alerta> comprobarAlertas(Set<Alerta> alertas, Long id) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        Set<Alerta> alertasUsuario = usuario.get().getAlertas();
        Set<Alerta> alertasDisponibles = new HashSet<>();
        for (Alerta alerta : alertas) {

            if (!alertasUsuario.contains(alerta)) {
                alertasDisponibles.add(alerta);
            }

        }
        return alertasDisponibles;

    }

    private Set<AlertaDTO> crearAlertasDTO(Set<Alerta> alertas) {

        Set<AlertaDTO> disponibles = new HashSet<>();


        for (Alerta alerta : alertas) {

            AlertaDTO alertaDTO = new AlertaDTO();

            alertaDTO.setId(Long.toString(alerta.getId()));
            alertaDTO.setNombre(alerta.getNombre());
            alertaDTO.setDescripcion(alerta.getDescripcion());
            disponibles.add(alertaDTO);
        }

        return disponibles;
    }

    private Set<Alerta> convertirListASet(List<Alerta> alertas) {

        Set<Alerta> alertaSet = new HashSet<>();

        alertaSet.addAll(alertas);

        return alertaSet;

    }

    public void actualizarAlerta(Long idUsuario, Long id, boolean add) {

        Optional<Usuario> usuario = usuarioDAO.findById(idUsuario);
        Optional<Alerta> alerta = alertaDAO.findById(id);

        Set<Usuario> usuariosAlerta = alerta.get().getUsuarios();
        Set<Alerta> alertasUsuario = usuario.get().getAlertas();
        System.out.println(id);

        if (add) {

            usuariosAlerta.add(usuario.get());
            alertasUsuario.add(alerta.get());

        } else if(!add) {

            usuariosAlerta.remove(usuario.get());
            alertasUsuario.remove(alerta.get());

        }

        alerta.get().setUsuarios(usuariosAlerta);
        usuario.get().setAlertas(alertasUsuario);

        alertaDAO.save(alerta.get());
        usuarioDAO.save(usuario.get());

    }

    public List<Usuario> buscarUsuariosConMasDeXAlertas(Long numeroAlertas) {

        List<Usuario> usuarios = (List<Usuario>) usuarioDAO.findAll();

        List<Usuario> usuariosEncontrados = new ArrayList<>();


        for (Usuario usuario : usuarios) {
            if (usuario.getAlertas().size() > numeroAlertas) {
                usuariosEncontrados.add(usuario);
            }
        }

        return usuariosEncontrados;

    }

    public List<Usuario> findUsuarioMaDeUnaPropiedad() {

        List<Usuario> usuarios = (List<Usuario>) usuarioDAO.findAll();

        List<Usuario> usuariosEncontrados = new ArrayList<>();


        for (Usuario usuario : usuarios) {
            if (usuario.getPropiedades().size() > 1) {
                usuariosEncontrados.add(usuario);
            }
        }

        return usuariosEncontrados;


    }

    public void habilitarDeshabilitarUsuario(Long id, boolean enabled) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if (usuario.isPresent()) {
            if (enabled) {

                usuario.get().setHabilitado(true);

            } else if(!enabled) {
                usuario.get().setHabilitado(false);

            }
            usuarioDAO.save(usuario.get());
        }

    }
}
