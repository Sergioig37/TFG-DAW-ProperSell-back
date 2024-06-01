package com.example.start.controller;

import com.example.start.dao.AgenteDAO;
import com.example.start.dao.ClienteDAO;
import com.example.start.dao.InmobiliariaDAO;
import com.example.start.dao.PropiedadDAO;
import com.example.start.entity.Agente;
import com.example.start.entity.Cliente;
import com.example.start.entity.Inmobiliaria;
import com.example.start.entity.Propiedad;
import org.apache.tomcat.util.http.parser.HttpParser;
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
    InmobiliariaDAO inmobiDAO;

    @Autowired
    PropiedadDAO propiedadDAO;

    @Autowired
    AgenteDAO agenteDAO;

    @Autowired
    ClienteDAO clienteDAO;

    @GetMapping("/inmobiliaria/{numeroAgentes}")
    public ResponseEntity<List<Inmobiliaria>> getNumeroInmobiliaria(@PathVariable Long numeroAgentes){

        List<Inmobiliaria> inmobiliarias= inmobiDAO.findInmobiliariasByNumeroEmpleadosMayorQue(numeroAgentes);

        if(inmobiliarias!=null){
            return ResponseEntity.status(HttpStatus.OK).body(inmobiliarias);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/propiedad/{precio}")
    public ResponseEntity<List<Propiedad>> getNumeroPropiedades(@PathVariable Long precio) {

        List<Propiedad> propiedades = propiedadDAO.findPropiedadesByPrecioMayorQue(precio);

        if (propiedades != null) {
            return ResponseEntity.status(HttpStatus.OK).body(propiedades);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
    @GetMapping("/agentesTrabajando")
    public ResponseEntity<List<Agente>> getNumeroAgentesTrabajando(){


            List<Agente> agentes = agenteDAO.findAgentesConClientes();

            if(agentes!=null){
                return ResponseEntity.status(HttpStatus.OK).body(agentes);
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
    }
    @GetMapping("/clientesMasDeUnaPropiedad")
    public ResponseEntity<List<Cliente>> getClientesMasDeUnaPropiedad(){


        List<Cliente> clientes = clienteDAO.findClientesConMasDeUnaPropiedad();

        if(clientes!=null){
            return ResponseEntity.status(HttpStatus.OK).body(clientes);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }


    @GetMapping("/agentesMasDeUnCliente")
    public ResponseEntity<List<Agente>> getAgentesConMasDeUnCliente(){


        List<Agente> agentes = agenteDAO.findAgentesConMasDeUnCliente();

        if(agentes!=null){
            return ResponseEntity.status(HttpStatus.OK).body(agentes);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @GetMapping("/clientesConMismoAgente")
    public ResponseEntity<List<Cliente>> getClientesConMismoAgente(){


        List<Cliente> clientes = clienteDAO.findClientesQueCompartenAgente();

        if(clientes!=null){
            return ResponseEntity.status(HttpStatus.OK).body(clientes);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}
