package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.*;
import es.proyecto.sergio.dao.AlertaDAO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.entity.Alerta;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.exception.CorreoYaExisteException;
import es.proyecto.sergio.exception.DatosNoValidosException;
import es.proyecto.sergio.exception.UsuarioYaExisteException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

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
        log.info("Inicio de borrado de usuario con ID: {} por el usuario: {}", id, username);
        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);

        if (usuarioOptional.isPresent()) {
            if (usuarioOptional.get().getUsername().equals(username)) {
                usuarioDAO.delete(usuarioOptional.get());
                log.info("Usuario con ID: {} borrado con éxito", id);
                return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
            }
            log.warn("Usuario con ID: {} no encontrado o no coincide el username", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            log.warn("Usuario con ID: {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<?> editarUsuario(@Valid UsuarioDTO usuarioDTO, Long id, BindingResult bindingResult, String username) {
        log.info("Inicio de edición de usuario con ID: {}", id);

        try {
            this.yaExiste(usuarioDTO);
            this.validarDatos(usuarioDTO, bindingResult);

            Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
            if (usuarioOptional.isPresent()) {
                if (usuarioOptional.get().getUsername().equals(username)) {
                    Usuario existingUser = usuarioOptional.get();
                    existingUser.setCorreo(usuarioDTO.getCorreo());
                    existingUser.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
                    existingUser.setUsername(usuarioDTO.getUsername());
                    log.debug("Editando usuario: {}", existingUser);
                    if (!usuarioDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
                    }
                    existingUser.setNombreReal(usuarioDTO.getNombreReal());
                    usuarioDAO.save(existingUser);

                    log.info("Usuario con ID: {} editado con éxito", id);
                    return ResponseEntity.status(HttpStatus.OK).body(this.lanzarNuevoToken(usuarioDTO));
                }
                log.warn("Usuario con ID: {} no encontrado o no coincide el username", id);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                log.warn("Usuario con ID: {} no encontrado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (DatosNoValidosException | UsuarioYaExisteException | CorreoYaExisteException e) {
            log.error("Error al editar usuario: {}", e.getMessage(), e);
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
        log.info("Obteniendo propiedades del usuario con ID: {}", id);
        Optional<Usuario> usuario = usuarioDAO.findById(id);

        List<PropiedadDTO> propiedades = new ArrayList<>();
        if (usuario.isPresent()) {
            propiedades = propiedadService.convertirAListaPropiedadDTO(usuario.get().getPropiedades());
            log.info("Propiedades obtenidas con éxito para el usuario con ID: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        } else {
            log.warn("Usuario con ID: {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<UsuarioDTO> getNumeroPropietario(@PathVariable Long id) {
        log.info("Obteniendo número de propietario para el usuario con ID: {}", id);
        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioDTO usuarioDTO = convertirAUsuarioDTO(usuarioOptional.get());
            log.info("Número de propietario obtenido con éxito para el usuario con ID: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDTO);
        } else {
            log.warn("Usuario con ID: {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public Set<AlertaDTO> getAlertasUsuario(@PathVariable Long id) {
        log.info("Obteniendo alertas del usuario con ID: {}", id);
        Optional<Usuario> usuario = usuarioDAO.findById(id);

        Set<Alerta> alertas = usuario.get().getAlertas();
        Set<AlertaDTO> alertasUsuarioDTO = this.crearAlertasDTO(alertas);

        log.info("Alertas obtenidas con éxito para el usuario con ID: {}", id);
        return alertasUsuarioDTO;
    }

    public Set<AlertaDTO> getAlertasDisponibles(Long id) {
        log.info("Obteniendo alertas disponibles para el usuario con ID: {}", id);
        List<Alerta> alertasTotalesList = (List<Alerta>) alertaDAO.findAll();

        Set<Alerta> alertasTotales = this.convertirListASet(alertasTotalesList);
        Set<Alerta> alertasRestantes = this.comprobarAlertas(alertasTotales, id);
        Set<AlertaDTO> alertasRestantesDTO = this.crearAlertasDTO(alertasRestantes);

        log.info("Alertas disponibles obtenidas con éxito para el usuario con ID: {}", id);
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
        log.info("Actualizando alerta con ID: {} para el usuario con ID: {}", id, idUsuario);
        Optional<Usuario> usuario = usuarioDAO.findById(idUsuario);
        Optional<Alerta> alerta = alertaDAO.findById(id);

        Set<Usuario> usuariosAlerta = alerta.get().getUsuarios();
        Set<Alerta> alertasUsuario = usuario.get().getAlertas();

        if (add) {
            usuariosAlerta.add(usuario.get());
            alertasUsuario.add(alerta.get());
            log.info("Alerta añadida con éxito al usuario con ID: {}", idUsuario);
        } else {
            usuariosAlerta.remove(usuario.get());
            alertasUsuario.remove(alerta.get());
            log.info("Alerta eliminada con éxito del usuario con ID: {}", idUsuario);
        }

        alerta.get().setUsuarios(usuariosAlerta);
        usuario.get().setAlertas(alertasUsuario);

        alertaDAO.save(alerta.get());
        usuarioDAO.save(usuario.get());
    }

    public List<UsuarioDTO> buscarUsuariosConMasDeXAlertas(Long numeroAlertas) {
        log.info("Buscando usuarios con más de {} alertas", numeroAlertas);
        List<Usuario> usuarios = (List<Usuario>) usuarioDAO.findAll();
        List<UsuarioDTO> usuarioDTOS = this.convertirAListaUsuarioDTO(usuarios);
        List<UsuarioDTO> usuariosEncontrados = new ArrayList<>();

        for (UsuarioDTO usuario : usuarioDTOS) {
            if (usuario.getNumeroAlertas() > numeroAlertas) {
                usuariosEncontrados.add(usuario);
            }
        }

        log.info("Usuarios encontrados con más de {} alertas: {}", numeroAlertas, usuariosEncontrados.size());
        return usuariosEncontrados;
    }

    public List<Usuario> findUsuarioMaDeUnaPropiedad() {
        log.info("Buscando usuarios con más de una propiedad");
        List<Usuario> usuarios = (List<Usuario>) usuarioDAO.findAll();
        List<Usuario> usuariosEncontrados = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.getPropiedades().size() > 1) {
                usuariosEncontrados.add(usuario);
            }
        }

        log.info("Usuarios encontrados con más de una propiedad: {}", usuariosEncontrados.size());
        return usuariosEncontrados;
    }

    public void habilitacionUsuario(Long id, boolean enabled) {
        log.info("Cambiando estado de habilitación del usuario con ID: {} a {}", id, enabled);
        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if (usuario.isPresent()) {
            usuario.get().setHabilitado(enabled);
            usuarioDAO.save(usuario.get());
            log.info("Estado de habilitación del usuario con ID: {} cambiado a {}", id, enabled);
        } else {
            log.warn("Usuario con ID: {} no encontrado", id);
        }
    }

    private void validarDatos(@Valid UsuarioDTO usuarioDTO, BindingResult bindingResult) throws DatosNoValidosException {
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación: {}", bindingResult.getAllErrors());
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        }
    }

    public ResponseEntity<?> existe(Long id) {
        log.info("Comprobando existencia del usuario con ID: {}", id);
        Optional<Usuario> usuario = usuarioDAO.findById(id);

        if (usuario.isPresent()) {
            UsuarioDTO usuarioDTO = this.convertirAUsuarioDTO(usuario.get());
            log.info("Usuario con ID: {} encontrado", id);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDTO);
        } else {
            log.warn("Usuario con ID: {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private void yaExiste(UsuarioDTO usuarioDTO) throws UsuarioYaExisteException, CorreoYaExisteException {
        log.info("Comprobando si ya existe un usuario con el username: {} o correo: {}", usuarioDTO.getUsername(), usuarioDTO.getCorreo());
        Optional<Usuario> usuarioComparar = usuarioDAO.findByUsername(usuarioDTO.getUsername());
        Optional<Usuario> usuario = usuarioDAO.findById(usuarioDTO.getId());
        Optional<Usuario> usuarioCompararCorreo = usuarioDAO.findByCorreo(usuarioDTO.getCorreo());

        if (usuarioComparar.isPresent() && usuario.isPresent()) {
            if (!usuarioComparar.get().getId().equals(usuario.get().getId())) {
                log.warn("Usuario ya existe con el username: {}", usuarioDTO.getUsername());
                throw new UsuarioYaExisteException("Este usuario ya existe");
            }
        }
        if (usuarioCompararCorreo.isPresent() && usuario.isPresent()) {
            if (!usuarioCompararCorreo.get().getId().equals(usuario.get().getId())) {
                log.warn("Correo ya existe: {}", usuarioDTO.getCorreo());
                throw new CorreoYaExisteException("Este correo ya existe");
            }
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
