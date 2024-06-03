package com.example.start.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.start.agentecliente.AlertaClienteKey;
import com.example.start.dao.AlertaClienteDAO;
import com.example.start.entity.AlertaCliente;

@Controller
public class AlertaClienteController {

	@Autowired
	private AlertaClienteDAO agenteClienteDAO;



//	@GetMapping("/agenteCliente")
//	public ResponseEntity<List<AlertaCliente>> getClientes() {
//		List<AlertaCliente> agentesClientes = (List<AlertaCliente>) agenteClienteDAO.findAll();
//		return ResponseEntity.ok().body(agentesClientes);
//	}
//
//	@GetMapping("/agenteCliente/del/{idCliente}/{idAgente}")
//	public ResponseEntity<Void> delAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {
//		AlertaClienteKey key = new AlertaClienteKey();
//		key.setAgenteId(idAgente);
//		key.setClienteId(idCliente);
//		agenteClienteDAO.deleteById(key);
//		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//	}
//
//
//
//	@PostMapping("/agenteCliente/save")
//	public ResponseEntity<Void> saveAgenteCliente(@RequestBody AgenteClienteDTO agenteClienteDTO) {
//		AlertaClienteKey key = new AlertaClienteKey();
//		AlertaCliente agenteCliente = new AlertaCliente();
//
//		Optional<Cliente> cliente = clienteDAO.findById(Long.valueOf(agenteClienteDTO.getCliente()));
//		Optional<Agente> agente = agenteDAO.findById(Long.valueOf(agenteClienteDTO.getCliente()));
//
//
//		agenteCliente.setAgente(agente.get());
//		agenteCliente.setCliente(cliente.get());
//		key.setClienteId(Long.valueOf(agenteClienteDTO.getCliente()));
//		key.setAgenteId(Long.valueOf(agenteClienteDTO.getAgente()));
//
//		agenteCliente.setId(key);
//		agenteClienteDAO.save(agenteCliente);
//		return ResponseEntity.status(HttpStatus.OK).body(null);
//	}
//
//	@GetMapping("/agenteCliente/{idCliente}/{idAgente}")
//	public ResponseEntity<AlertaCliente> getAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {
//		AlertaClienteKey key = new AlertaClienteKey();
//		key.setAgenteId(idAgente);
//		key.setClienteId(idCliente);
//		Optional<AlertaCliente> agenteCliente = agenteClienteDAO.findById(key);
//		return agenteCliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//	}
//
//	@GetMapping("/agenteCliente/agentes/{cliente}")
//	public ResponseEntity<List<Agente>> getAgentesDeXCliente(@PathVariable long cliente){
//
//		List<Agente> agentes = agenteClienteDAO.findAgentesByClienteId(cliente);
//
//		if(agentes==null){
//			return ResponseEntity.status(HttpStatus.OK).body(null);
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(agentes);
//
//	}
//
//	@GetMapping("/agenteCliente/clientes/{agente}")
//	public ResponseEntity<List<Cliente>> getClientesDeXAgente(@PathVariable long agente){
//
//		List<Cliente> clientes = agenteClienteDAO.findClientesByAgenteId(agente);
//
//		if(clientes==null){
//			return ResponseEntity.status(HttpStatus.OK).body(null);
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(clientes);
//
//	}
//
//	@GetMapping("/agenteCliente/clientesLibres/{agente}")
//	public ResponseEntity<List<Cliente>> getClientesNotWorkingWithAgente(@PathVariable Long agente){
//
//		List<Cliente> clientes = agenteClienteDAO.findClientesNotWorkingWithAgente(agente);
//
//		if(clientes==null){
//			return ResponseEntity.status(HttpStatus.OK).body(null);
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(clientes);
//
//	}

}
