package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.PropiedadDTO;
import com.example.start.entity.Usuario;
import com.example.start.exception.DatosNoValidosException;
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

import com.example.start.dao.PropiedadDAO;
import com.example.start.entity.Propiedad;
import com.example.start.service.PropiedadService;

import jakarta.validation.Valid;


@RestController
public class PropiedadController {

	@Autowired
	PropiedadDAO propiedadDAO;
	@Autowired
	UsuarioDAO usuarioDAO;
	@Autowired
	PropiedadService propiedadService;
	
	
	
	@GetMapping("/propiedad")
	public ResponseEntity<List<Propiedad>> getPropiedades(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findAll());
	}
	
	
	@GetMapping("/propiedad/{id}")
	public ResponseEntity<Propiedad> getPropiedad(@PathVariable Long id){

		return ResponseEntity.status(HttpStatus.OK).body(propiedadDAO.findById(id).get());
		
	}
	
	
	@DeleteMapping("/propiedad/del/{id}")
	public ResponseEntity delPropiedad(@PathVariable Long id) {

			propiedadService.deletePropiedad(id);

	    	return ResponseEntity.status(HttpStatus.OK).body(null);

	}
	
	
	@PostMapping("/propiedad/save")
	public ResponseEntity<?> savePropiedad(@RequestBody @Valid  PropiedadDTO propiedadDTO, BindingResult bindingResult){


            return propiedadService.enlazarPropietario(propiedadDTO, bindingResult);

		
	}
	
	
	@PutMapping("/propiedad/edit/{id}")
	public ResponseEntity<?> editPropiedad(@RequestBody @Valid PropiedadDTO propiedadDTO, @PathVariable Long id, BindingResult bindingResult){
		

		
		return propiedadService.actualizar(propiedadDTO, id, bindingResult);
		
		
	}
	
	@GetMapping("/propiedad/propietario/{idPropiedad}")
	public ResponseEntity<Usuario> getVerificarPropietario(@PathVariable Long idPropiedad){


		Optional<Usuario> usuario = propiedadDAO.findPropietarioByPropiedadId(idPropiedad);

		if(usuario.isPresent()){
			return
					ResponseEntity.status(HttpStatus.OK).body(usuario.get());
		}
		else {
			return
					ResponseEntity.status(HttpStatus.OK).body(null);

		}
		

	}

	@GetMapping("/propiedadExcluida/{id}")
	public ResponseEntity<List<Propiedad>> getPropiedadesQueNoSonDeEstePropietario(@PathVariable Long id){

		Optional<Usuario> user = usuarioDAO.findById(id);

		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findPropiedadesQueNoSonDelPropietario(user.get().getId()));
	}



	@GetMapping("/propiedad/disable/{idPropiedad}")
	public ResponseEntity deshabilitarPropiedad(@PathVariable Long idPropiedad){

		propiedadService.deshabilitarPropiedad(idPropiedad);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}




	@GetMapping("/propiedad/enable/{idPropiedad}")
	public ResponseEntity habilitarUsuario(@PathVariable Long idPropiedad){

		propiedadService.habilitarPropiedad(idPropiedad);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}
