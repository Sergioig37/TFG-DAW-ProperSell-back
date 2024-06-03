package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.start.dao.ClienteDAO;
import com.example.start.entity.Cliente;

import jakarta.validation.Valid;


@RestController
public class ClienteController {

	@Autowired
	ClienteDAO clienteDAO;
	
	 
	@GetMapping("/cliente")
	public ResponseEntity<List<Cliente>> getClientes(){

		return ResponseEntity.status(HttpStatus.OK).body((List<Cliente>)clienteDAO.findAll());
	}
	
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> getCliente(@PathVariable Long id){

		Optional<Cliente> clienteOptional = clienteDAO.findById(id);

		if(clienteOptional.isPresent()){
			return ResponseEntity.status(HttpStatus.OK).body(clienteOptional.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
	}
	
	
	@DeleteMapping("/cliente/del/{id}")
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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cliente);
		}
		
		clienteDAO.save(cliente);
		
		return ResponseEntity.status(HttpStatus.OK).body(cliente);
		
		
	}
	
	@PutMapping("/cliente/edit/{id}")
	public ResponseEntity<Cliente> editCliente(@RequestBody @Valid Cliente cliente, @PathVariable Long id){
		
		Optional<Cliente> clienteExiste =  clienteDAO.findById(id);
		
		if(clienteExiste.isPresent()) {
			
			clienteExiste.get().setNombre( cliente.getNombre());
			clienteExiste.get().setCorreo(cliente.getCorreo());
			clienteExiste.get().setNumeroTelefono(cliente.getNumeroTelefono());
			clienteDAO.save(clienteExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(clienteExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		
	}

	@GetMapping("/clienteNumero/{propiedad}")
	public ResponseEntity<Cliente> getNumeroCliente(@PathVariable Long propiedad){

		Cliente cliente = clienteDAO.findClienteByPropiedadId(propiedad);

		String telefono = cliente.getNumeroTelefono();

		return ResponseEntity.status(HttpStatus.OK).body(cliente);

	}
	
}
