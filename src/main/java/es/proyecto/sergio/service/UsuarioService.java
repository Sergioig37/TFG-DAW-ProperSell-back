package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.*;
import es.proyecto.sergio.dao.AlertaDAO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.entity.Alerta;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.exception.DatosNoValidosException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
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
    PropiedadService propiedadService;

    @Autowired
    LoginService loginService;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<?> borrarUsuario(Long id, String username) {
        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);

        if (usuarioOptional.isPresent()) {
            if (usuarioOptional.get().getUsername().equals(username)) {
                usuarioDAO.delete(usuarioOptional.get());
                return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<?> editarUsuario(@Valid UsuarioDTO usuarioDTO, Long id, BindingResult bindingResult, String username) {


        try {

            this.validarDatos(usuarioDTO, bindingResult);


            Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
            if (usuarioOptional.isPresent()) {
                if (usuarioOptional.get().getUsername().equals(username)) {
                    Usuario existingUser = usuarioOptional.get();
                    existingUser.setCorreo(usuarioDTO.getCorreo());
                    existingUser.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
                    System.out.println(usuarioDTO.getPassword());
                    if (usuarioDTO.getPassword() != "") {
                        System.out.println(usuarioDTO.getPassword());
                        existingUser.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
                    }
                    existingUser.setNombreReal(usuarioDTO.getNombreReal());
                    usuarioDAO.save(existingUser);

                    return ResponseEntity.status(HttpStatus.OK).body(this.lanzarNuevoToken(usuarioDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (DatosNoValidosException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }


    }

    private AuthResponseDTO lanzarNuevoToken(UsuarioDTO usuarioDTO) {

        LoginRequestDTO loginRequest = new LoginRequestDTO();

        loginRequest.setUsername(usuarioDTO.getUsername());
        loginRequest.setPassword(usuarioDTO.getPassword());

        return loginService.autenticar(loginRequest);

    }

    public ResponseEntity<List<PropiedadDTO>> getPropiedadesDelUsuario(Long id) {
        Optional<Usuario> usuario = usuarioDAO.findById(id);


        List<PropiedadDTO> propiedades = new ArrayList<>();
        if (usuario.isPresent()) {
            propiedades = propiedadService.convertirAListaPropiedadDTO(usuario.get().getPropiedades());
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<UsuarioDTO> getNumeroPropietario(@PathVariable Long id) {

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);


        if (usuarioOptional.isPresent()) {
            UsuarioDTO usuariodto = convertirAUsuarioDTO(usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(usuariodto);
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

            alertaDTO.setId(alerta.getId());
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

        } else if (!add) {

            usuariosAlerta.remove(usuario.get());
            alertasUsuario.remove(alerta.get());

        }

        alerta.get().setUsuarios(usuariosAlerta);
        usuario.get().setAlertas(alertasUsuario);

        alertaDAO.save(alerta.get());
        usuarioDAO.save(usuario.get());

    }

    public List<UsuarioDTO> buscarUsuariosConMasDeXAlertas(Long numeroAlertas) {

        List<Usuario> usuarios = (List<Usuario>) usuarioDAO.findAll();

        List<UsuarioDTO> usuarioDTOS = this.convertirAListaUsuarioDTO(usuarios);

        List<UsuarioDTO> usuariosEncontrados = new ArrayList<>();


        for (UsuarioDTO usuario : usuarioDTOS) {
            if (usuario.getNumeroAlertas() > numeroAlertas) {
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

    public void habilitacionUsuario(Long id, boolean enabled) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if (usuario.isPresent()) {
            if (enabled) {

                usuario.get().setHabilitado(true);

            } else if (!enabled) {
                usuario.get().setHabilitado(false);

            }
            usuarioDAO.save(usuario.get());
        }

    }

    private void validarDatos(@Valid UsuarioDTO usuarioDTO, BindingResult bindingResult) throws DatosNoValidosException {
        if (bindingResult.hasErrors()) {
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        }
    }

    public ResponseEntity<?> existe(Long id) {

        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if (usuario.isPresent()) {

            UsuarioDTO usuarioDTO = this.convertirAUsuarioDTO(usuario.get());

            return ResponseEntity.status(HttpStatus.OK).body(usuarioDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    public List<UsuarioDTO> convertirAListaUsuarioDTO(List<Usuario> usuarios) {

        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        for (Usuario usuario : usuarios) {


            UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);

            usuarioDTO.setNumeroAlertas(usuario.getAlertas().size());

            usuarioDTO.setNumeroPropiedades(usuario.getPropiedades().size());
            usuariosDTO.add(usuarioDTO);

        }


        return usuariosDTO;


    }

    public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {

        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);

        usuarioDTO.setNumeroAlertas(usuario.getAlertas().size());

        usuarioDTO.setNumeroPropiedades(usuario.getPropiedades().size());

        return usuarioDTO;
    }

}
