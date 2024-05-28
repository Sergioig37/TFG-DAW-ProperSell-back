package com.example.start.auth;

import java.util.Optional;

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
	
	public AuthResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		Usuario user = new Usuario();
		
		user.setCorreo(request.getCorreo());
		user.setNombreReal(request.getNombreReal());
		user.setPassword(bcryptPasswordEncoder.encode(request.getPassword()));
		user.setUsername(request.getUsername());
		if(request.getUsername().equals("SergioAdmin")) {
			user.setRole(Role.ADMIN);
		}else {
			user.setRole(Role.USER );
		}
		
		
		usuarioDAO.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

	public AuthResponse login(LoginRequest request) throws Exception{
		// TODO Auto-generated method stub
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		Optional<Usuario> user = usuarioDAO.findByUsername(request.getUsername());
		
		if(user.isPresent()) {
			String token = jwtService.getToken((UserDetails) user.get());
			return AuthResponse.builder().token(token).build();
		}
		
		return null;
	}

}
