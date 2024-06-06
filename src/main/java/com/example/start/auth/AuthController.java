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
	public ResponseEntity login(@RequestBody LoginRequest request) throws Exception {

		try{
			AuthResponse token = authService.login(request);
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	
	}
	
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody RegisterRequest request) throws Exception {

		try {
			authService.register(request);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

		}
	}
		
}
