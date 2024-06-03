package com.example.start.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.start.entity.Usuario;
import com.example.start.dao.UsuarioDAO;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	 UsuarioDAO usuarioDAO;
	
	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<Usuario> usuario = usuarioDAO.findByUsername(correo);
		
		if(usuario.isPresent()) {
			return 	(UserDetails)usuario.get();
					
		}
		
		throw new UsernameNotFoundException("El usuario " + correo +" no existe");
		
	}
}
