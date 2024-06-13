package es.proyecto.sergio.controller;

import es.proyecto.sergio.service.AuthService;
import es.proyecto.sergio.dto.LoginRequestDTO;
import es.proyecto.sergio.dto.RegisterRequestDTO;
import es.proyecto.sergio.service.RegisterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger
            = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {



       return authService.login(request);

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO request, BindingResult bindingResult)  {

       return registerService.manejarRegistro(request, bindingResult);

    }

}
