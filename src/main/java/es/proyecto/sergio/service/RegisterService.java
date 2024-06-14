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

@Service
public class RegisterService {

    private static final Logger log = LoggerFactory.getLogger(RegisterService.class);

    @Autowired
    AuthService authService;

    public ResponseEntity<?> manejarRegistro(@Valid RegisterRequestDTO request, BindingResult bindingResult){
        log.info("Inicio de manejo de registro para el usuario: {}", request.getCorreo());

        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación: {}", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de registro no válidos");
        }

        try {
            log.debug("Intentando registrar al usuario: {}", request.getCorreo());
            authService.register(request, bindingResult);
            log.info("Usuario registrado con éxito: {}", request.getCorreo());
            return ResponseEntity.status(HttpStatus.OK).body("Usuario registrado con éxito");
        } catch (UsuarioYaExisteException | CorreoYaExisteException e) {
            log.warn("Error de conflicto de registro para el usuario: {}", request.getCorreo(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DatosNoValidosException e) {
            log.error("Datos no válidos para el usuario: {}", request.getCorreo(), e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado durante el registro del usuario: {}", request.getCorreo(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }
}
