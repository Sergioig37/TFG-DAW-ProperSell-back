package com.example.start.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.start.dao.UsuarioDAO;
import com.example.start.entidades.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

	@Autowired
	UsuarioDAO usuarioDAO;
	
	@GetMapping("/usuario")
	public ResponseEntity<List<Usuario>> getUsuarios(){
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Usuario>)usuarioDAO.findAll());
		
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> getUsuario(@PathVariable String username){
		
		Optional<Usuario> usuario = usuarioDAO.findById(username);
		
		if(usuario.isPresent()) {
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
	}
	
	@GetMapping("/usuario/del/{id}")
	public ResponseEntity<Usuario> deleteUsuario(@PathVariable String username){
		
		Optional<Usuario> usuario = usuarioDAO.findById(username);
		
		if(usuario.isPresent()) {
			usuarioDAO.delete(usuario.get());
			return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(null);

		
	}
	
	@PostMapping("/usuario/save")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody @Valid Usuario usuario, HttpServletRequest request) {
	    // Verifica si el usuario ha pasado por el proceso de registro
	    Boolean usuarioValidado = (Boolean) request.getSession().getAttribute("usuarioValidado");
	    if (Boolean.TRUE.equals(usuarioValidado)) {
	        usuarioDAO.save(usuario);
	        // Limpia la variable de sesión después de guardar el usuario
	        request.getSession().removeAttribute("usuarioValidado");
	        return ResponseEntity.status(HttpStatus.OK).body(usuario);
	    } else {
	        // Redirige al usuario a la ruta de registro si no ha pasado por el proceso
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
	}
	
}
