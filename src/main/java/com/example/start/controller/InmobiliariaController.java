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

import com.example.start.dao.InmobiliariaDAO;
import com.example.start.entity.Inmobiliaria;

import jakarta.validation.Valid;


@RestController
public class InmobiliariaController {

	@Autowired 
	InmobiliariaDAO inmobiDAO;
	
	@GetMapping("/inmobiliaria")
	public ResponseEntity<List<Inmobiliaria>> getInmobiliarias(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Inmobiliaria>)inmobiDAO.findAll());
	}
	
	@GetMapping("/inmobiliaria/{id}")
	public ResponseEntity<Inmobiliaria> getInmobiliaria(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(inmobiDAO.findById(id).get());
		
	}
	
	
	@DeleteMapping("/inmobiliaria/del/{id}")
	public ResponseEntity<Inmobiliaria> delInmobiliarias(@PathVariable Long id) {
		
		
		Optional<Inmobiliaria> inmobiliaria = inmobiDAO.findById(id);
		
		if(inmobiliaria.isPresent()) {
			inmobiDAO.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(inmobiliaria.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
	}
	
	@PostMapping("/inmobiliaria/save")
	public ResponseEntity<Inmobiliaria> saveInmobiliaria(@RequestBody @Valid Inmobiliaria inmobiliaria, BindingResult bindingResult) {
		
		
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(inmobiliaria);
		}
		
		inmobiDAO.save(inmobiliaria);
		
		return ResponseEntity.status(HttpStatus.OK).body(inmobiliaria);
	}
	
	@PutMapping("/inmobiliaria/edit/{id}")
	public ResponseEntity<Inmobiliaria> editInmobiliaria(@RequestBody Inmobiliaria inmobiliaria, @PathVariable Long id){
		
		
		Optional<Inmobiliaria> inmobiliariaExiste = inmobiDAO.findById(id);
		
		if(inmobiliariaExiste.isPresent()) {
			inmobiliariaExiste.get().setDireccion(inmobiliaria.getDireccion());
			inmobiliariaExiste.get().setDireccion(inmobiliaria.getDireccion());
			inmobiliariaExiste.get().setNombre(inmobiliaria.getNombre());
			inmobiliariaExiste.get().setNumeroEmpleados(inmobiliaria.getNumeroEmpleados());
			inmobiDAO.save(inmobiliariaExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(inmobiliariaExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	
	}
	
	
}
