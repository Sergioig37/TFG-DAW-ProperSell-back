package com.example.start.auth;

import com.example.start.exception.CorreoYaExisteException;
import com.example.start.exception.UsuarioYaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginRequest request)  {

			AuthResponse token = authService.login(request);
			return ResponseEntity.status(HttpStatus.OK).body(token);

	}


	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			authService.register(request);
			return ResponseEntity.status(HttpStatus.OK).body("Usuario registrado con éxito");
		} catch (UsuarioYaExisteException | CorreoYaExisteException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
		}
	}
		
}
