package com.example.start.controller;

import com.example.start.dao.AlertaDAO;
import com.example.start.dto.AlertaDTO;
import com.example.start.entity.Alerta;
import com.example.start.service.AlertaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AlertaController {

    @Autowired
    AlertaDAO alertaDAO;

    @Autowired
    AlertaService alertaService;

    @GetMapping("/alerta")
    public ResponseEntity<List<Alerta>> getAlertas() {


        return ResponseEntity.status(HttpStatus.OK).body((List<Alerta>) alertaDAO.findAll());
    }


    @GetMapping("/alerta/{id}")
    public ResponseEntity<Alerta> getAlerta(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(alertaDAO.findById(id).get());

    }


    @DeleteMapping("/alerta/del/{id}")
    public ResponseEntity<?> delAlerta(@PathVariable Long id) {

        alertaService.borrarAlerta(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }


    @PostMapping("/alerta/save")
    public ResponseEntity saveAlerta(@RequestBody @Valid AlertaDTO alertaDTO, BindingResult bindingResult) {


        return alertaService.guardarAlerta(alertaDTO, bindingResult);
    }

    @PutMapping("/alerta/edit/{id}")
    public ResponseEntity<?> editAlerta(@RequestBody @Valid AlertaDTO alertaDTO, @PathVariable Long id, BindingResult bindingResult) {


        return alertaService.editarAlerta(alertaDTO, id, bindingResult);

    }
}
