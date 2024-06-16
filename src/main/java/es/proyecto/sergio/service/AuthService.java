package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.LoginRequestDTO;
import es.proyecto.sergio.dto.RegisterRequestDTO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.entity.Rol;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.exception.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service // Marca esta clase como un servicio de Spring
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class); // Logger para registrar mensajes de error y depuración

    @Autowired
    UsuarioDAO usuarioDAO; // DAO para acceder y gestionar usuarios en la base de datos

    @Autowired
    JwtService jwtService; // Servicio para gestionar operaciones relacionadas con JWT

    @Autowired
    AuthenticationManager authManager; // Gestor de autenticación de Spring Security

    @Autowired
    PasswordEncoder bcryptPasswordEncoder; // Codificador de contraseñas para encriptar y verificar contraseñas

    @Autowired
    LoginService loginService; // Servicio para gestionar el proceso de autenticación

    // Método para registrar un nuevo usuario
    public void register(@Valid RegisterRequestDTO request, BindingResult bindingResult)
            throws CorreoYaExisteException, UsuarioYaExisteException, DatosNoValidosException {

        Usuario user = new Usuario(); // Crea una nueva instancia de Usuario

        // Verifica si hay errores de validación en el formulario de registro
        if (bindingResult.hasErrors()) {
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        } else {
            // Verifica si el nombre de usuario ya está en uso
            if (usuarioDAO.findByUsername(request.getUsername()).isPresent()) {
                throw new UsuarioYaExisteException("Este usuario ya está en uso");
            }
            // Verifica si el correo electrónico ya está en uso
            else if (usuarioDAO.findByCorreo(request.getCorreo()).isPresent()) {
                throw new CorreoYaExisteException("Este correo ya está en uso");
            }
        }

        // Establece los detalles del nuevo usuario
        user.setCorreo(request.getCorreo());
        user.setNombreReal(request.getNombreReal());
        user.setPassword(bcryptPasswordEncoder.encode(request.getPassword())); // Encripta la contraseña
        user.setUsername(request.getUsername());
        user.setRole(Rol.USER); // Asigna el rol de usuario
        user.setHabilitado(true); // Habilita el usuario
        user.setNumeroTelefono(request.getNumeroTelefono());

        // Guarda el nuevo usuario en la base de datos
        usuarioDAO.save(user);
    }

    // Método para iniciar sesión
    public ResponseEntity<?> login(LoginRequestDTO request) {
        try {
            // Llama al servicio de autenticación y devuelve una respuesta HTTP con el resultado
            return ResponseEntity.status(HttpStatus.OK).body(loginService.autenticar(request));
        } catch (DisabledException e) {
            log.error("Error DisabledException en login"); // Registra el error
            log.error(e.getMessage());
            UsuarioBloqueadoException ue = new UsuarioBloqueadoException("Este usuario está bloqueado");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ue.getMessage()); // Devuelve una respuesta HTTP con el mensaje de error
        } catch (BadCredentialsException e) {
            log.error("Error BadCredentialsException en login"); // Registra el error
            log.error(e.getMessage());
            UsuarioPasswordException ue = new UsuarioPasswordException("Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ue.getMessage()); // Devuelve una respuesta HTTP con el mensaje de error
        } catch (Exception e) {
            log.error("Error desconocido en login"); // Registra el error
            log.error(e.getMessage());
            return null; // Devuelve una respuesta nula en caso de error desconocido
        }
    }
}
