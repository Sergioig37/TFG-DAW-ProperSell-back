package es.proyecto.sergio.controller;

import es.proyecto.sergio.dao.AlertaDAO;
import es.proyecto.sergio.dto.AlertaDTO;
import es.proyecto.sergio.entity.Alerta;
import es.proyecto.sergio.service.AlertaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alerta")
public class AlertaController {

    @Autowired
    AlertaDAO alertaDAO;

    @Autowired
    AlertaService alertaService;

    private static final Logger logger
            = LoggerFactory.getLogger(AlertaController.class);

    @GetMapping
    public ResponseEntity<List<Alerta>> getAlertas() {



        List<Alerta> alertas = (List<Alerta>) alertaDAO.findAll();

        logger.info("Sacando alertas");
        logger.error("No se pudo sacar las alertas");

        return ResponseEntity.status(HttpStatus.OK).body(alertas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Alerta> getAlerta(@PathVariable Long id) {

        logger.info("Mostrando alerta");

        Optional<Alerta> alerta = alertaDAO.findById(id);



        return ResponseEntity.status(HttpStatus.OK).body(alertaDAO.findById(id).get());

    }


    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteAlerta(@PathVariable Long id) {

        alertaService.borrarAlerta(id);


        return ResponseEntity.status(HttpStatus.OK).body(null);

    }


    @PostMapping("/save")
    public ResponseEntity saveAlerta(@RequestBody @Valid AlertaDTO alertaDTO, BindingResult bindingResult) {


        return alertaService.guardarAlerta(alertaDTO, bindingResult);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editAlerta(@RequestBody @Valid AlertaDTO alertaDTO, @PathVariable Long id, BindingResult bindingResult) {


        return alertaService.editarAlerta(alertaDTO, id, bindingResult);

    }
}
