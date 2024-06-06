package com.example.start.auth;

import java.util.Optional;

import com.example.start.exception.CorreoYaExisteException;
import com.example.start.exception.PasswordNoExisteException;
import com.example.start.exception.UsuarioNoExisteException;
import com.example.start.exception.UsuarioYaExisteException;
import com.mysql.cj.exceptions.PasswordExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.start.jwt.JwtService;
import com.example.start.user.Role;
import com.example.start.entity.Usuario;
import com.example.start.dao.UsuarioDAO;


@Service
public class AuthService {

	@Autowired
	UsuarioDAO usuarioDAO;

	@Autowired
	JwtService jwtService;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	PasswordEncoder bcryptPasswordEncoder;

	public void register(RegisterRequest request) throws Exception {
		// TODO Auto-generated method stub
		Usuario user = new Usuario();
		if (usuarioDAO.findByUsername(request.getUsername()).isPresent()) {
			throw new UsuarioYaExisteException("Este usuario ya está en uso");
		} else if (usuarioDAO.findByCorreo(request.getCorreo()).isPresent()) {
			throw new CorreoYaExisteException("Este correo ya está en uso");
		}
		user.setCorreo(request.getCorreo());
		user.setNombreReal(request.getNombreReal());
		user.setPassword(bcryptPasswordEncoder.encode(request.getPassword()));
		user.setUsername(request.getUsername());
		user.setRole(Role.USER);
		user.setHabilitado(true);
		user.setNumeroTelefono(request.getNumeroTelefono());



		usuarioDAO.save(user);

	}


	public AuthResponse login(LoginRequest request) {
		// TODO Auto-generated method stub


		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));


		Optional<Usuario> user = usuarioDAO.findByUsername(request.getUsername());

		if (user.isPresent() ) {
			if(bcryptPasswordEncoder.matches(request.getPassword(), user.get().getPassword())){
				String token = jwtService.getToken((UserDetails) user.get());
				return AuthResponse.builder().token(token).build();
			}
			else{
				//throw new PasswordNoExisteException("Contraseña incorrecta");
			}

		}
		else{
			//throw new UsuarioNoExisteException("Usuario incorrecto" );
		}

	return null;
	}
}



