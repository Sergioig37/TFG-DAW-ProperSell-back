package com.example.start.utility;

import java.util.List;
import java.util.Optional;

import com.example.start.dao.ClienteDAO;
import com.example.start.entity.Cliente;
import com.example.start.entity.Propiedad;

public final class MetodosPropiedad {

	private MetodosPropiedad() {

	}

	public static MetodosPropiedad getInstancia() {
		return new MetodosPropiedad();
	}

	public void desenlazarPropietario(Propiedad propiedad, ClienteDAO clienteDAO) {

		if (propiedad.getPropietario() != null) {
			Optional<Cliente> cliente = clienteDAO.findById(propiedad.getPropietario().getId());

			List<Propiedad> propiedades = cliente.get().getPropiedades();
			propiedades.remove(propiedad);
			cliente.get().setPropiedades(propiedades);

			clienteDAO.save(cliente.get());
		}

	}
}
