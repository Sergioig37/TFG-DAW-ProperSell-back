package com.example.start.utility;

import java.util.List;
import java.util.Optional;

import com.example.start.dao.PropiedadDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;

public final class MetodosPropiedad {

	private MetodosPropiedad() {

	}

	public static MetodosPropiedad getInstancia() {
		return new MetodosPropiedad();
	}

	public void desenlazarPropietario(Propiedad propiedad, UsuarioDAO usuarioDAO) {

		if (propiedad.getPropietario() != null) {
			Optional<Usuario> cliente = usuarioDAO.findById(propiedad.getPropietario().getId());

			List<Propiedad> propiedades = cliente.get().getPropiedades();
			propiedades.remove(propiedad);
			cliente.get().setPropiedades(propiedades);

			usuarioDAO.save(cliente.get());
		}

	}

	public Boolean comprobarPertenencia(Usuario usuario, Propiedad propiedad){

		if(usuario.getPropiedades().contains(propiedad)){
			return true;
		}
		else{
			return false;
		}

	}
}
