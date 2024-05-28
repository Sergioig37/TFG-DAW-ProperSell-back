package com.example.start.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

	
	@GetMapping("/")
	public ResponseEntity<String> publi() {
		System.out.println("PASANDO POAR AQUÍ");
		return ResponseEntity.status(HttpStatus.OK).body("Correcto");
	}
}
