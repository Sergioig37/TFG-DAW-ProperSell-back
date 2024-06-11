package es.proyecto.sergio.controller;

import java.util.List;
import java.util.Optional;

import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.dto.PropiedadDTO;
import es.proyecto.sergio.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import es.proyecto.sergio.dao.PropiedadDAO;
import es.proyecto.sergio.entity.Propiedad;
import es.proyecto.sergio.service.PropiedadService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/propiedad")
public class PropiedadController {

	@Autowired
	PropiedadDAO propiedadDAO;
	@Autowired
	UsuarioDAO usuarioDAO;
	@Autowired
	PropiedadService propiedadService;

	private static final Logger logger
			= LoggerFactory.getLogger(PropiedadController.class);

	@GetMapping
	public ResponseEntity<List<Propiedad>> getPropiedades(){


		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findAll());
	}


	@GetMapping("/habilitadas")
	public ResponseEntity<List<Propiedad>> getPropiedadesHabilitadas(){


		return ResponseEntity.status(HttpStatus.OK).body((List<Propiedad>)propiedadDAO.findPropiedadesHabilitadas());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPropiedad(@PathVariable Long id){

		return propiedadService.existe(id);

	}


	@DeleteMapping("/del/{id}/{username}")
	public ResponseEntity deletePropiedad(@PathVariable Long id, @PathVariable String username) {

			propiedadService.deletePropiedad(id, username);

	    	return ResponseEntity.status(HttpStatus.OK).body(null);

	}


	@PostMapping("/save")
	public ResponseEntity<?> savePropiedad(@RequestBody @Valid  PropiedadDTO propiedadDTO, BindingResult bindingResult){


            return propiedadService.enlazarPropietario(propiedadDTO, bindingResult);


	}


	@PutMapping("/edit/{id}/{username}")
	public ResponseEntity<?> editPropiedad(@RequestBody @Valid PropiedadDTO propiedadDTO, @PathVariable Long id, BindingResult bindingResult,@PathVariable String username){



		return propiedadService.actualizar(propiedadDTO, id, bindingResult, username);


	}

	@GetMapping("/propietario/{idPropiedad}")
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



	@PutMapping("/enabled/{idPropiedad}/{enabled}")
	public ResponseEntity<?> edithabilitacionPropiedad(@PathVariable Long idPropiedad, @PathVariable boolean enabled){
		System.out.println(enabled);
		propiedadService.habilitacionPropiedad(idPropiedad, enabled);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
