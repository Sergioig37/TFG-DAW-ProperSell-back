package com.example.start.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.start.dao.AgenteDAO;
import com.example.start.dao.InmobiliariaDAO;
import com.example.start.entidades.Agente;
import com.example.start.entidades.Inmobiliaria;
import com.example.start.utilidades.MetodosAgente;

import jakarta.validation.Valid;

@Controller
public class AgenteController {

	@Autowired 
	AgenteDAO agenteDAO;
	@Autowired
	InmobiliariaDAO inmobiDAO;
	
	@GetMapping("/agente")
	public ResponseEntity<List<Agente>> getAgentes(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Agente>)agenteDAO.findAll());
	}
	
	
	@GetMapping("/agente/{id}")
	public ResponseEntity<Agente> getAgente(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(agenteDAO.findById(id).get());
		
	}
	
	
	@GetMapping("/agente/del/{id}")
	public ResponseEntity<Agente> delAgente(@PathVariable Long id) {
		
		
		Optional<Agente> agenteOptional = agenteDAO.findById(id);
	    
	    if (agenteOptional.isPresent()) {
	    	
	    	MetodosAgente.getInstancia().desenlazarInmobiliaria(agenteOptional.get(), inmobiDAO);
	        agenteDAO.delete(agenteOptional.get());
	        return ResponseEntity.status(HttpStatus.OK).body(agenteOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	/*
	@PostMapping("/agente/save")
	public ResponseEntity<Agente> saveAgente(@RequestBody @Valid Agente agente, BindingResult bindingResult) {
		
		
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(agente);
		}
		
		System.out.println(agente.getInmobiliaria());
		
		
		agenteDAO.save(agente);
		
		return ResponseEntity.status(HttpStatus.OK).body(agente);
	}
	
	@GetMapping("/agente/edit/{id}")
	public ResponseEntity<Agente> editAgente(@RequestBody Agente agente, @PathVariable Long id){
		
		
		Optional<Agente> agenteExiste = agenteDAO.findById(id);
		
		if(agenteExiste.isPresent()) {
			
			agenteExiste.get().setNombre(agente.getNombre());
			agenteExiste.get().setNumeroTelefono(agente.getNumeroTelefono());
			agenteDAO.save(agenteExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(agenteExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	
	}
	*/
}
