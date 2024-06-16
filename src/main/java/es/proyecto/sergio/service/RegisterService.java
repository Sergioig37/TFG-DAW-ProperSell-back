package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.RegisterRequestDTO;
import es.proyecto.sergio.exception.CorreoYaExisteException;
import es.proyecto.sergio.exception.DatosNoValidosException;
import es.proyecto.sergio.exception.UsuarioYaExisteException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service // Marca esta clase como un servicio de Spring
public class RegisterService {

    private static final Logger log = LoggerFactory.getLogger(RegisterService.class); // Logger para registrar mensajes de error y depuración

    @Autowired
    AuthService authService; // Servicio de autenticación para manejar el registro de usuarios

    // Método para manejar el registro de un usuario
    public ResponseEntity<?> manejarRegistro(@Valid RegisterRequestDTO request, BindingResult bindingResult) {
        log.info("Inicio de manejo de registro para el usuario: {}", request.getCorreo()); // Registra el inicio del proceso de registro

        // Verifica si hay errores de validación en el formulario de registro
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación: {}", bindingResult.getAllErrors()); // Registra los errores de validación
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de registro no válidos"); // Devuelve una respuesta HTTP con el mensaje de error
        }

        try {
            log.debug("Intentando registrar al usuario: {}", request.getUsername()); // Registra la intención de registrar el usuario
            authService.register(request, bindingResult); // Intenta registrar al usuario usando AuthService
            log.info("Usuario registrado con éxito: {}", request.getUsername()); // Registra el éxito del registro
            return ResponseEntity.status(HttpStatus.OK).body("Usuario registrado con éxito"); // Devuelve una respuesta HTTP con el mensaje de éxito
        } catch (UsuarioYaExisteException | CorreoYaExisteException e) {
            log.warn("Error de conflicto de registro para el usuario: {}", request.getUsername(), e); // Registra el conflicto de registro
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Devuelve una respuesta HTTP con el mensaje de conflicto
        } catch (DatosNoValidosException e) {
            log.error("Datos no válidos para el usuario: {}", request.getUsername(), e); // Registra los datos no válidos
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage()); // Devuelve una respuesta HTTP con el mensaje de datos no válidos
        } catch (Exception e) {
            log.error("Error inesperado durante el registro del usuario: {}", request.getUsername(), e); // Registra cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado"); // Devuelve una respuesta HTTP con el mensaje de error inesperado
        }
    }
}
