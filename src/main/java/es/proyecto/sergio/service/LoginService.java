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

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    JwtService jwtService;

    public AuthResponseDTO autenticar(LoginRequestDTO request) throws DisabledException, BadCredentialsException {


        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Optional<Usuario> user = usuarioDAO.findByUsername(request.getUsername());
        String token = jwtService.getToken((UserDetails) user.get());
        return AuthResponseDTO.builder().token(token).build();
    }

}
