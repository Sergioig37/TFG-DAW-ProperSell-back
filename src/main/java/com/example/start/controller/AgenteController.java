package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import com.example.start.dto.AgenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.start.dao.AgenteDAO;
import com.example.start.entity.Agente;


@RestController
public class AgenteController {

	@Autowired 
	AgenteDAO agenteDAO;

	
	
	@GetMapping("/agente")
	public ResponseEntity<List<Agente>> getAgentes(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Agente>)agenteDAO.findAll());
	}
	
	
	@GetMapping("/agente/{id}")
	public ResponseEntity<Agente> getAgente(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(agenteDAO.findById(id).get());
		
	}
	
	
	@DeleteMapping("/agente/del/{id}")
	public ResponseEntity<Agente> delAgente(@PathVariable Long id) {
		
		Optional<Agente> agenteOptional = agenteDAO.findById(id);

	    if (agenteOptional.isPresent()) {

	        agenteDAO.delete(agenteOptional.get());

	        return ResponseEntity.status(HttpStatus.OK).body(agenteOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	
	@PostMapping("/agente/save")
	public ResponseEntity saveAgente(@RequestBody AgenteDTO agenteDTO) {

		Agente agente = new Agente();



		agente.setNombre(agenteDTO.getNombre());
		agente.setCorreo(agenteDTO.getCorreo());
		agente.setNumeroTelefono(agenteDTO.getNumeroTelefono());

		agenteDAO.save(agente);
		System.out.println("LIST");
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PutMapping("/agente/edit/{id}")
	public ResponseEntity<Agente> editAgente(@RequestBody Agente agente, @PathVariable Long id){
		

		Optional<Agente> agenteExiste = agenteDAO.findById(id);
		
		if(agenteExiste.isPresent()) {
			agenteExiste.get().setNombre(agente.getNombre());
			agenteExiste.get().setNumeroTelefono(agente.getNumeroTelefono());
			agenteExiste.get().setCorreo(agente.getCorreo());
			agenteDAO.save(agenteExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(agenteExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	
	}
	
}
