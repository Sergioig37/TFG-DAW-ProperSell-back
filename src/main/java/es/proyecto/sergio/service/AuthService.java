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


@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    PasswordEncoder bcryptPasswordEncoder;


    @Autowired
    LoginService loginService;

    public void register(@Valid RegisterRequestDTO request, BindingResult bindingResult) throws CorreoYaExisteException, UsuarioYaExisteException, DatosNoValidosException {

        Usuario user = new Usuario();

        if (bindingResult.hasErrors()) {
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        } else {
            if (usuarioDAO.findByUsername(request.getUsername()).isPresent()) {
                throw new UsuarioYaExisteException("Este usuario ya está en uso");
            } else if (usuarioDAO.findByCorreo(request.getCorreo()).isPresent()) {
                throw new CorreoYaExisteException("Este correo ya está en uso");
            }
        }

        user.setCorreo(request.getCorreo());
        user.setNombreReal(request.getNombreReal());
        user.setPassword(bcryptPasswordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRole(Rol.USER);
        user.setHabilitado(true);
        user.setNumeroTelefono(request.getNumeroTelefono());


        usuarioDAO.save(user);

    }


    public ResponseEntity<?> login(LoginRequestDTO request){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(loginService.autenticar(request));
        } catch (DisabledException e) {
            log.error("Error DisabledException en login");
            log.error(e.getMessage());
            UsuarioBloqueadoException ue = new UsuarioBloqueadoException("Este usuario está bloqueado");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ue.getMessage());
        } catch (BadCredentialsException e) {
            log.error("Error BadCredentialsException en login");
            log.error(e.getMessage());
            UsuarioPasswordException ue = new UsuarioPasswordException("Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ue.getMessage());
        } catch (Exception e) {
            log.error("Error desconocido en login");
            log.error(e.getMessage());
            return null;
        }

    }
}



