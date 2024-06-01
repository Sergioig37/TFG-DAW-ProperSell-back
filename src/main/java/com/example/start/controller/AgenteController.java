package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import com.example.start.dto.AgenteDTO;
import com.example.start.entity.Inmobiliaria;
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

import com.example.start.dao.AgenteDAO;
import com.example.start.dao.InmobiliariaDAO;
import com.example.start.entity.Agente;
import com.example.start.utility.MetodosAgente;




@RestController
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
	
	
	@DeleteMapping("/agente/del/{id}")
	public ResponseEntity<Agente> delAgente(@PathVariable Long id) {
		
		System.out.println("Empezando delete");
		
		System.out.println("Empezando delete");

		
		Optional<Agente> agenteOptional = agenteDAO.findById(id);
		System.out.println("Buscando agente para borrar");
	    if (agenteOptional.isPresent()) {
	    	System.out.println("Agente encontrado");
	    	System.out.println("Desvinculando");
	    	MetodosAgente.getInstancia().desenlazarInmobiliaria(agenteOptional.get(), inmobiDAO);
	    	System.out.println("Agente desvinculado");
	    	
	    	System.out.println("Borrando...");
	        agenteDAO.delete(agenteOptional.get());
	        System.out.println("Agente borrado...");
	        return ResponseEntity.status(HttpStatus.OK).body(agenteOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	
	@PostMapping("/agente/save")
	public ResponseEntity saveAgente(@RequestBody AgenteDTO agenteDTO) {
		System.out.println("HOLA, ENTRANDO");
		Agente agente = new Agente();


		System.out.println("EMPEZANDO A SETEAR");
		agente.setNombre(agenteDTO.getNombre());
		agente.setCorreo(agenteDTO.getCorreo());
		agente.setNumeroTelefono(agenteDTO.getNumeroTelefono());
		System.out.println("SETTEADO");
		// Crear el agente
		// Asignar otros datos del agente desde request

		// Asignar la inmobiliaria al agente
		Inmobiliaria inmobiliaria = inmobiDAO.findById(Long.valueOf(agenteDTO.getInmobiliaria())).orElse(null);
		inmobiDAO.save(inmobiliaria);
		System.out.println("SETEANDO INMOBILIARIA");
		agente.setInmobiliaria(inmobiliaria);
		System.out.println("GAURDADNO");
		
		
		agenteDAO.save(agente);
		System.out.println("LIST");
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PutMapping("/agente/edit/{id}")
	public ResponseEntity<Agente> editAgente(@RequestBody Agente agente, @PathVariable Long id){
		
		System.out.println("Encontrando agente");
		Optional<Agente> agenteExiste = agenteDAO.findById(id);
		
		if(agenteExiste.isPresent()) {
			System.out.println("Agente Encontrado");
			System.out.println("Actualizando");
			agenteExiste.get().setNombre(agente.getNombre());
			agenteExiste.get().setNumeroTelefono(agente.getNumeroTelefono());
			agenteExiste.get().setCorreo(agente.getCorreo());
			System.out.println("Actuaklizado...");
			System.out.println("Guardando...");
			agenteDAO.save(agenteExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(agenteExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	
	}
	
}
