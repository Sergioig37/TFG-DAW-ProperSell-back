package com.example.start.controller;

import java.util.List;
import java.util.Optional;

import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.PropiedadDTO;
import com.example.start.entity.Usuario;
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

import com.example.start.dao.PropiedadDAO;
import com.example.start.entity.Propiedad;
import com.example.start.utility.MetodosPropiedad;

import jakarta.validation.Valid;


@RestController
public class PropiedadController {

	@Autowired
	PropiedadDAO propiedadDAO;
	@Autowired
	UsuarioDAO usuarioDAO;
	
	
	
	@GetMapping("/propiedad")
	public ResponseEntity<List<Propiedad>> getPropiedades(){
		
		
		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findAll());
	}
	
	
	@GetMapping("/propiedad/{id}")
	public ResponseEntity<Propiedad> getPropiedad(@PathVariable Long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(propiedadDAO.findById(id).get());
		
	}
	
	
	@DeleteMapping("/propiedad/del/{id}")
	public ResponseEntity<Propiedad> delPropiedad(@PathVariable Long id) {
		
		
		Optional<Propiedad> propiedadOptional = propiedadDAO.findById(id);
	    
	    if (propiedadOptional.isPresent()) {
	    	
	    	MetodosPropiedad.getInstancia().desenlazarPropietario(propiedadOptional.get(), usuarioDAO);
	        propiedadDAO.delete(propiedadOptional.get());
	        return ResponseEntity.status(HttpStatus.OK).body(propiedadOptional.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
			   
	}
	
	
	@PostMapping("/propiedad/save")
	public ResponseEntity<Propiedad> savePropiedad(@RequestBody PropiedadDTO propiedadDTO){

		Propiedad propiedad = new Propiedad();

		propiedad.setTipo(propiedadDTO.getTipo());
		propiedad.setLocalizacion(propiedadDTO.getLocalizacion());
		propiedad.setPrecio(Long.valueOf(propiedadDTO.getPrecio()));

		if(!propiedadDTO.getPropietario().equals("")){
			Optional<Usuario> cliente = usuarioDAO.findByUsername(propiedadDTO.getPropietario());
			usuarioDAO.save(cliente.get());
			propiedad.setPropietario(cliente.get());
		}

		propiedadDAO.save(propiedad);
		
		return ResponseEntity.status(HttpStatus.OK).body(propiedad);
		
		
	}
	
	
	@PutMapping("/propiedad/edit/{id}")
	public ResponseEntity<Propiedad> editPropiedad(@RequestBody @Valid Propiedad propiedad, @PathVariable Long id){
		
		Optional<Propiedad> propiedadExiste =  propiedadDAO.findById(id);
		
		if(propiedadExiste.isPresent()) {
			
			propiedadExiste.get().setLocalizacion(propiedad.getLocalizacion());
			propiedadExiste.get().setPrecio(propiedad.getPrecio());
			propiedadExiste.get().setTipo(propiedad.getTipo());
			
			propiedadDAO.save(propiedadExiste.get());
			return ResponseEntity.status(HttpStatus.OK).body(propiedadExiste.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		
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

	@GetMapping("/propiedadExcluida/{username}")
	public ResponseEntity<List<Propiedad>> getPropiedadesQueNoSonDeEstePropietario(@PathVariable String username){

		Optional<Usuario> user = usuarioDAO.findByUsername(username);

		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findPropiedadesQueNoSonDelPropietario(user.get().getId()));
	}

	@GetMapping("/propiedad/disable/{idPropiedad}")
	public ResponseEntity deshabilitarUsuario(@PathVariable Long idPropiedad){

		Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

		if(propiedad.isPresent()){
			propiedad.get().setHabilitado(false);
			propiedadDAO.save(propiedad.get());
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/propiedad/enable/{idPropiedad}")
	public ResponseEntity habilitarUsuario(@PathVariable Long idPropiedad){

		Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

		if(propiedad.isPresent()){
			propiedad.get().setHabilitado(true);
			propiedadDAO.save(propiedad.get());
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}
