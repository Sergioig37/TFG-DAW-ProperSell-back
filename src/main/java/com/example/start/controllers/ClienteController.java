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

import com.example.start.dao.ClienteDAO;
import com.example.start.entidades.Agente;
import com.example.start.entidades.Cliente;

import jakarta.validation.Valid;


@Controller
public class ClienteController {

	@Autowired
	ClienteDAO clienteDAO;
	
	@GetMapping("/cliente")
	public ResponseEntity<List<Cliente>> getClientes(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Cliente>)clienteDAO.findAll());
	}
	
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> getCliente(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(clienteDAO.findById(id).get());
		
	}
	
	
	@GetMapping("/cliente/del/{id}")
	public ResponseEntity<Cliente> delCliente(@PathVariable Long id) {
		
		
		Optional<Cliente>  clienteOptional = clienteDAO.findById(id);
	    
	    if (clienteOptional.isPresent()) {
	        clienteDAO.delete(clienteOptional.get());
	        return ResponseEntity.status(HttpStatus.OK).body(clienteOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	@PostMapping("/cliente/save")
	public ResponseEntity<Cliente> saveCliente(@RequestBody @Valid Cliente cliente, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(cliente);
		}
		
		clienteDAO.save(cliente);
		
		return ResponseEntity.status(HttpStatus.OK).body(cliente);
		
		
	}
	
	@GetMapping("/cliente/edit/{id}")
	public ResponseEntity<Cliente> editCliente(@RequestBody @Valid Cliente cliente, @PathVariable Long id){
		
		Optional<Cliente> clienteExiste =  clienteDAO.findById(id);
		
		if(clienteExiste.isPresent()) {
			
			clienteExiste.get().setNombre( cliente.getNombre());
			clienteDAO.save(clienteExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(clienteExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		
	}
	
}
