package com.example.start.auth;

import com.example.start.exception.CorreoYaExisteException;
import com.example.start.exception.DatosNoValidosException;
import com.example.start.exception.UsuarioYaExisteException;
import com.example.start.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

	@Autowired
	RegisterService registerService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {

        AuthResponse token = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult)  {

       return registerService.manejarErrores(request, bindingResult);

    }

}
