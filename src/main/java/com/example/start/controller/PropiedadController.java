package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.start.dao.ClienteDAO;
import com.example.start.dao.PropiedadDAO;
import com.example.start.entity.Propiedad;
import com.example.start.utility.MetodosPropiedad;

import jakarta.validation.Valid;


@RestController
public class PropiedadController {

	@Autowired
	PropiedadDAO propiedadDAO;
	@Autowired
	ClienteDAO clienteDAO;
	
	
	
	@GetMapping("/propiedad")
	public ResponseEntity<List<Propiedad>> getClientes(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findAll());
	}
	
	
	@GetMapping("/propiedad/{id}")
	public ResponseEntity<Propiedad> getCliente(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(propiedadDAO.findById(id).get());
		
	}
	
	
	@DeleteMapping("/propiedad/del/{id}")
	public ResponseEntity<Propiedad> delCliente(@PathVariable Long id) {
		
		
		Optional<Propiedad> propiedadOptional = propiedadDAO.findById(id);
	    
	    if (propiedadOptional.isPresent()) {
	    	
	    	MetodosPropiedad.getInstancia().desenlazarPropietario(propiedadOptional.get(), clienteDAO);
	        propiedadDAO.delete(propiedadOptional.get());
	        return ResponseEntity.status(HttpStatus.OK).body(propiedadOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	
	@PostMapping("/propiedad/save")
	public ResponseEntity<Propiedad> savePropiedad(@RequestBody @Valid Propiedad propiedad, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(propiedad);
		}
		
		propiedadDAO.save(propiedad);
		
		return ResponseEntity.status(HttpStatus.OK).body(propiedad);
		
		
	}
	
	
	@PutMapping("/propiedad/edit/{id}")
	public ResponseEntity<Propiedad> editPropiedad(@RequestBody @Valid Propiedad propiedad, @PathVariable Long id){
		
		Optional<Propiedad> propiedadExiste =  propiedadDAO.findById(id);
		
		if(propiedadExiste.isPresent()) {
			
			propiedadExiste.get().setLocalizacion(propiedad.getLocalizacion());
			propiedadExiste.get().setPrecio(propiedad.getPrecio());
			propiedadExiste.get().setTipo(propiedad.getTipo());
			
			propiedadDAO.save(propiedadExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(propiedadExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		
	}
	
	
	
}
