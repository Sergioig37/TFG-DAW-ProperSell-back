package com.example.start.utility;

import java.util.List;
import java.util.Optional;

import com.example.start.dao.ClienteDAO;
import com.example.start.dao.PropiedadDAO;
import com.example.start.entity.Cliente;
import com.example.start.entity.Propiedad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

public final class MetodosPropiedad {

	@Autowired
	PropiedadDAO propiedadDAO;

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

	public List<Propiedad> filtrarPropiedad(String localizacion, String precioMin, String precioMax) {

		List<Propiedad> propiedades;

		if (localizacion.equals("void")) {
			if (precioMin.equals("void") && precioMax.equals("void")) {
				// No se ha especificado ningún criterio de búsqueda
				propiedades = (List<Propiedad>) propiedadDAO.findAll();
				return propiedades;
			}
			else if (precioMax.equals("void")){
				propiedades = propiedadDAO.findByPrecioMin(Long.valueOf(precioMin));
				return propiedades;
			}
			else if(precioMin.equals("void")){
				propiedades = propiedadDAO.findByPrecioMax(Long.valueOf(precioMax));
				return propiedades;
			}
			else {
				// Búsqueda solo por rango de precio
				propiedades = propiedadDAO.findByPrecioRange(Long.valueOf(precioMin), Long.valueOf(precioMax));
				return propiedades;
			}
		} else {
			if (precioMin.equals("void") && precioMax.equals("void")) {
				// Búsqueda solo por localización
				propiedades = propiedadDAO.findByLocalizacionAndPrecioRange(localizacion, null, null);
				return propiedades;
			} else if (precioMin.equals("void")) {
				// Búsqueda por localización y rango máximo de precio
				propiedades = propiedadDAO.findByLocalizacionAndPrecioMax(localizacion, Long.valueOf(precioMax));
				return propiedades;
			} else if (precioMax.equals("void")) {
				// Búsqueda por localización y rango mínimo de precio
				propiedades = propiedadDAO.findByLocalizacionAndPrecioMin(localizacion, Long.valueOf(precioMin));
				return propiedades;
			} else {
				// Búsqueda por localización y rango de precio completo
				propiedades = propiedadDAO.findByLocalizacionAndPrecioRange(localizacion, Long.valueOf(precioMin), Long.valueOf(precioMax));
				return propiedades;
			}
		}



	}
}
