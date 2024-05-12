package com.spring.start.seguridad;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.start.exceptions.EmailNotFoundException;
import com.spring.start.usuarios.Usuario;
import com.spring.start.usuarios.UsuarioDAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UsuarioDAO usuarioDAO;
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Optional<Usuario> usuario = usuarioDAO.findById(username);

	        if(usuario.isPresent()) {
	            Usuario user = usuario.get();
	            return (UserDetails)user;
	        }

	        throw new UsernameNotFoundException("El usuario con nombre "+ username +" no existe");
	    }

	    public UserDetails loadUserByCorreo(String correo) throws EmailNotFoundException {
	        Optional<Usuario> usuario = usuarioDAO.findByCorreo(correo);
	        if(usuario.isPresent()) {
	            Usuario user = usuario.get();
	            return (UserDetails)user;
	        } else {
	            throw new EmailNotFoundException("El correo electrónico no existe");
	        }
	    }
	
	
	private ResponseEntity<String> signUp(Map<String, String> requestMap){
		return null;
	}
	
	
	private Usuario getUserFromMap(Map<String, String> requestMap) {
		
		Usuario user= new Usuario();
		user.setPassword(requestMap.get("password"));
		user.setUsuario(requestMap.get("usuario"));
		return user;
	}
}
