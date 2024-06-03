package com.example.start.controller;

import com.example.start.dao.PropiedadDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.entity.Propiedad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {


    @Autowired
    PropiedadDAO propiedadDAO;



    @Autowired
    UsuarioDAO usuarioDAO;



    @GetMapping("/propiedad/{precio}")
    public ResponseEntity<List<Propiedad>> getNumeroPropiedades(@PathVariable Long precio) {

        List<Propiedad> propiedades = propiedadDAO.findPropiedadesByPrecioMayorQue(precio);

        if (propiedades != null) {
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }






}
