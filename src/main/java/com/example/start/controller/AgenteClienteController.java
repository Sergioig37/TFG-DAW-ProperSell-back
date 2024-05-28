package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.start.agentecliente.AgenteClienteKey;
import com.example.start.dao.AgenteClienteDAO;
import com.example.start.dao.AgenteDAO;
import com.example.start.dao.ClienteDAO;
import com.example.start.entity.AgenteCliente;


@Controller
public class AgenteClienteController {

	@Autowired
	AgenteClienteDAO agenteClienteDAO;
	@Autowired
	AgenteDAO agenteDAO;
	@Autowired
	ClienteDAO clienteDAO;

	@GetMapping("/agenteCliente")
	public ModelAndView getClientes() {

		ModelAndView model = new ModelAndView();
		model.setViewName("agenteClientes");
		List<AgenteCliente> agentesClientes = (List<AgenteCliente>) agenteClienteDAO.findAll();

		model.addObject("agentesClientes", agentesClientes);

		return model;
	}

	@GetMapping("/agenteCliente/del/{idCliente}/{idAgente}")
	public ModelAndView delAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {

		ModelAndView model = new ModelAndView();

		AgenteClienteKey key = new AgenteClienteKey();

		key.setAgenteId(idAgente);
		key.setClienteId(idCliente);

		agenteClienteDAO.deleteById(key);

		model.setViewName("redirect:/agenteCliente");

		return model;
	}

	@GetMapping("/agenteCliente/add")
	public ModelAndView addAgenteCliente() {

		ModelAndView model = new ModelAndView();
		model.setViewName("formAgenteCliente");
		model.addObject("agenteCliente", new AgenteCliente());
		model.addObject("agentes", agenteDAO.findAll());
		model.addObject("clientes", clienteDAO.findAll());

		return model;

	}

	@PostMapping("/agenteCliente/save")
	public ModelAndView saveAgenteCliente(@ModelAttribute AgenteCliente agenteCliente) {

		ModelAndView model = new ModelAndView();

		AgenteClienteKey key = new AgenteClienteKey();
		key.setClienteId(agenteCliente.getCliente().getId());
		key.setAgenteId(agenteCliente.getAgente().getId());

		agenteCliente.setId(key);

		agenteClienteDAO.save(agenteCliente);

		model.setViewName("redirect:/agente/" + agenteCliente.getAgente().getId());

		return model;
	}

	@GetMapping("/agenteCliente/{idCliente}/{idAgente}")
	public ModelAndView getAgenteCliente(@PathVariable long idCliente, @PathVariable long idAgente) {

		ModelAndView model = new ModelAndView();
		model.setViewName("agenteCliente");

		AgenteClienteKey key = new AgenteClienteKey();

		key.setAgenteId(idAgente);
		key.setClienteId(idCliente);
		
		Optional<AgenteCliente> agenteCliente = agenteClienteDAO.findById(key);
		
		if (agenteCliente.isPresent()) {
			model.addObject("agenteCliente", agenteCliente.get());
		}

		return model;
	}
}
