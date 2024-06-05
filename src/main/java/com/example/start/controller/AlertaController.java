package com.example.start.controller;

import com.example.start.dao.AlertaDAO;
import com.example.start.entity.Alerta;
import com.example.start.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Alerta>> getAgentes(){


        return ResponseEntity.status(HttpStatus.OK).body((List<Alerta>)alertaDAO.findAll());
    }


    @GetMapping("/alerta/{id}")
    public ResponseEntity<Alerta> getAgente(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK).body(alertaDAO.findById(id).get());

    }


    @DeleteMapping("/alerta/del/{id}")
    public ResponseEntity delAgente(@PathVariable Long id) {

       alertaService.borrarAlerta(id);

       return ResponseEntity.status(HttpStatus.OK).body(null);

    }


    @PostMapping("/alerta/save")
    public ResponseEntity saveAgente(@RequestBody Alerta alerta) {



        alertaDAO.save(alerta);
        System.out.println("LIST");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/alerta/edit/{id}")
    public ResponseEntity<Alerta> editAgente(@RequestBody Alerta alerta, @PathVariable Long id){


        Optional<Alerta> alertaExiste = alertaDAO.findById(id);

        if(alertaExiste.isPresent()) {
            alertaExiste.get().setDescripcion(alerta.getDescripcion());
            alertaExiste.get().setNombre(alerta.getNombre());
            alertaDAO.save(alertaExiste.get());
            return ResponseEntity.status(HttpStatus.OK).body(alertaExiste.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }
}
