package com.example.start.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.start.entidades.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/public")
public class RegistroController {

	@GetMapping("/registro")
	public String resgistrar(@RequestBody @Valid Usuario usuario, HttpServletRequest request) {
	
		request.getSession().setAttribute("usuarioValidado", true);
		
		return "redirect: /usuario/save";
		
	}
	
	@GetMapping("/")
	public String adslfm(){
		return "TODO BINE";
	}
}
