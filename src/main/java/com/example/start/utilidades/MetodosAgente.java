package com.example.start.utilidades;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.start.dao.AgenteDAO;
import com.example.start.dao.InmobiliariaDAO;
import com.example.start.entidades.Agente;
import com.example.start.entidades.Inmobiliaria;


public final class MetodosAgente {

	
	
	private MetodosAgente() {
		
	}
	
	
	public static MetodosAgente getInstancia() {
		return new MetodosAgente();
	}
	
	public void desenlazarInmobiliaria(Agente agente, InmobiliariaDAO inmobiDAO) {
		
		
		if (agente.getInmobiliaria() != null) {
			Optional<Inmobiliaria> inmobiliaria = inmobiDAO.findById(agente.getInmobiliaria().getId());

			List<Agente> agentes = inmobiliaria.get().getAgentes();
			agentes.remove(agente);
			inmobiliaria.get().setAgentes(agentes);

			inmobiDAO.save(inmobiliaria.get());
		}
			
			

		}
	
		public void enlazarInmobiliaria(Inmobiliaria inmobiliaria, InmobiliariaDAO inmobiDAO, Agente agente) {
			

			
			List<Agente> agentes = inmobiliaria.getAgentes();
			agentes.add(agente);
			inmobiliaria.setAgentes(agentes);
			inmobiDAO.save(inmobiliaria);
			
		}
	}

