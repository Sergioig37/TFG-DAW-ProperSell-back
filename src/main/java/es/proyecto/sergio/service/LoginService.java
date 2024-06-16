package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.AuthResponseDTO;
import es.proyecto.sergio.dto.LoginRequestDTO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Marca esta clase como un servicio de Spring
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class); // Logger para registrar mensajes de error y depuración

    @Autowired
    AuthenticationManager authManager; // Gestor de autenticación de Spring Security

    @Autowired
    UsuarioDAO usuarioDAO; // DAO para acceder y gestionar usuarios en la base de datos

    @Autowired
    JwtService jwtService; // Servicio para gestionar operaciones relacionadas con JWT

    // Método para autenticar un usuario
    public AuthResponseDTO autenticar(LoginRequestDTO request) throws DisabledException, BadCredentialsException {

        // Autentica el usuario con el nombre de usuario y la contraseña proporcionados
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Busca el usuario en la base de datos por nombre de usuario
        Optional<Usuario> user = usuarioDAO.findByUsername(request.getUsername());

        // Genera un token JWT para el usuario autenticado
        String token = jwtService.getToken((UserDetails) user.get());

        // Devuelve una respuesta de autenticación con el token generado
        return AuthResponseDTO.builder().token(token).build();
    }
}
