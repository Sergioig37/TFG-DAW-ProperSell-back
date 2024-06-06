package com.example.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.start.dao.PropiedadDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.PropiedadDTO;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public final class PropiedadService {


	@Autowired
	UsuarioDAO usuarioDAO;

	@Autowired
	PropiedadDAO propiedadDAO;


	private void desenlazarPropietario(Propiedad propiedad) {

		if (propiedad.getPropietario() != null) {
			Optional<Usuario> cliente = usuarioDAO.findById(propiedad.getPropietario().getId());

			List<Propiedad> propiedades = cliente.get().getPropiedades();
			propiedades.remove(propiedad);
			cliente.get().setPropiedades(propiedades);

			usuarioDAO.save(cliente.get());
		}

	}

	public void enlazarPropietario(PropiedadDTO propiedadDTO) {
		Propiedad propiedad = new Propiedad();

		propiedad.setTipo(propiedadDTO.getTipo());
		propiedad.setLocalizacion(propiedadDTO.getLocalizacion());
		propiedad.setPrecio(Long.valueOf(propiedadDTO.getPrecio()));
		propiedad.setHabilitado(true);

		if(!propiedadDTO.getPropietario().equals("")){
			Optional<Usuario> cliente = usuarioDAO.findById(Long.valueOf(propiedadDTO.getPropietario()));
			usuarioDAO.save(cliente.get());
			propiedad.setPropietario(cliente.get());
		}

		propiedadDAO.save(propiedad);
	}

	public void actualizar(PropiedadDTO propiedad, Long id) {

		Optional<Propiedad> propiedadExiste =  propiedadDAO.findById(id);

		if(propiedadExiste.isPresent()) {

			propiedadExiste.get().setLocalizacion(propiedad.getLocalizacion());
			propiedadExiste.get().setPrecio(Long.valueOf(propiedad.getPrecio()));
			propiedadExiste.get().setTipo(propiedad.getTipo());

			propiedadDAO.save(propiedadExiste.get());

		}

	}
	public void deshabilitarPropiedad(Long idPropiedad) {

		Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

		if(propiedad.isPresent()){
			propiedad.get().setHabilitado(false);
			propiedadDAO.save(propiedad.get());
		}

	}

	public void habilitarPropiedad(Long idPropiedad) {

		Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

		if(propiedad.isPresent()){
			propiedad.get().setHabilitado(true);
			propiedadDAO.save(propiedad.get());
		}

	}

	public ResponseEntity deletePropiedad(Long id) {

		Optional<Propiedad> propiedadOptional = propiedadDAO.findById(id);

		if (propiedadOptional.isPresent()) {

			this.desenlazarPropietario(propiedadOptional.get());
			propiedadDAO.delete(propiedadOptional.get());
			return ResponseEntity.status(HttpStatus.OK).body(propiedadOptional.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	public List<Propiedad> propiedadesMasCarasQue(Long precio){

		List<Propiedad> propiedades = propiedadDAO.findPropiedadesByPrecioMayorQue(precio);

		if (propiedades != null) {
			return propiedades;
		} else {
			return new ArrayList<Propiedad>();
		}

	}

}
