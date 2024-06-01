package com.example.start.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.start.agentecliente.AgenteClienteKey;
import com.example.start.dao.AgenteClienteDAO;
import com.example.start.dao.AgenteDAO;
import com.example.start.dao.ClienteDAO;
import com.example.start.entity.AgenteCliente;

@Controller
public class AgenteClienteController {

	@Autowired
	private AgenteClienteDAO agenteClienteDAO;

	@Autowired
	private AgenteDAO agenteDAO;

	@Autowired
	private ClienteDAO clienteDAO;

	@GetMapping("/agenteCliente")
	public ResponseEntity<List<AgenteCliente>> getClientes() {
		List<AgenteCliente> agentesClientes = (List<AgenteCliente>) agenteClienteDAO.findAll();
		return ResponseEntity.ok().body(agentesClientes);
	}

	@GetMapping("/agenteCliente/del/{idCliente}/{idAgente}")
	public ResponseEntity<Void> delAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {
		AgenteClienteKey key = new AgenteClienteKey();
		key.setAgenteId(idAgente);
		key.setClienteId(idCliente);
		agenteClienteDAO.deleteById(key);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/agenteCliente/add")
	public ResponseEntity<String> addAgenteCliente() {
		// Code for adding a new AgenteCliente
		return ResponseEntity.ok().body("Formulario de AgenteCliente");
	}

	@PostMapping("/agenteCliente/save")
	public ResponseEntity<Void> saveAgenteCliente(@RequestBody AgenteCliente agenteCliente) {
		AgenteClienteKey key = new AgenteClienteKey();
		key.setClienteId(agenteCliente.getCliente().getId());
		key.setAgenteId(agenteCliente.getAgente().getId());
		agenteCliente.setId(key);
		agenteClienteDAO.save(agenteCliente);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/agenteCliente/{idCliente}/{idAgente}")
	public ResponseEntity<AgenteCliente> getAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {
		AgenteClienteKey key = new AgenteClienteKey();
		key.setAgenteId(idAgente);
		key.setClienteId(idCliente);
		Optional<AgenteCliente> agenteCliente = agenteClienteDAO.findById(key);
		return agenteCliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
