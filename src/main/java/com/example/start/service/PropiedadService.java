package com.example.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.start.dao.PropiedadDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.dto.PropiedadDTO;
import com.example.start.dto.UsuarioDTO;
import com.example.start.entity.Propiedad;
import com.example.start.entity.Usuario;
import com.example.start.exception.DatosNoValidosException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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

    public ResponseEntity<?> enlazarPropietario(@Valid PropiedadDTO propiedadDTO, BindingResult bindingResult) {
        Propiedad propiedad = new Propiedad();

        try {
            this.validarDatos(propiedadDTO, bindingResult);
            propiedad.setTipo(propiedadDTO.getTipo());
            propiedad.setLocalizacion(propiedadDTO.getLocalizacion());
            propiedad.setPrecio(Long.valueOf(propiedadDTO.getPrecio()));
            propiedad.setHabilitado(true);

            if (!propiedadDTO.getPropietario().equals("")) {
                Optional<Usuario> cliente = usuarioDAO.findById(Long.valueOf(propiedadDTO.getPropietario()));
                usuarioDAO.save(cliente.get());
                propiedad.setPropietario(cliente.get());
            }

            propiedadDAO.save(propiedad);
            return  ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (DatosNoValidosException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }


    }

    public ResponseEntity<?> actualizar(@Valid PropiedadDTO propiedad, Long id, BindingResult bindingResult) {

        Optional<Propiedad> propiedadExiste = propiedadDAO.findById(id);

        try {
            this.validarDatos(propiedad, bindingResult);
            if (propiedadExiste.isPresent()) {

                propiedadExiste.get().setLocalizacion(propiedad.getLocalizacion());
                propiedadExiste.get().setPrecio(Long.valueOf(propiedad.getPrecio()));
                propiedadExiste.get().setTipo(propiedad.getTipo());

                propiedadDAO.save(propiedadExiste.get());

                return  ResponseEntity.status(HttpStatus.OK).body(null);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (DatosNoValidosException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }

    }

    public void deshabilitarPropiedad(Long idPropiedad) {

        Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

        if (propiedad.isPresent()) {
            propiedad.get().setHabilitado(false);
            propiedadDAO.save(propiedad.get());
        }

    }

    public void habilitarPropiedad(Long idPropiedad) {

        Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

        if (propiedad.isPresent()) {
            propiedad.get().setHabilitado(true);
            propiedadDAO.save(propiedad.get());
        }

    }

    public ResponseEntity<?> deletePropiedad(Long id) {

        Optional<Propiedad> propiedadOptional = propiedadDAO.findById(id);

        if (propiedadOptional.isPresent()) {

            this.desenlazarPropietario(propiedadOptional.get());
            propiedadDAO.delete(propiedadOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(propiedadOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public List<Propiedad> propiedadesMasCarasQue(Long precio) {

        List<Propiedad> propiedades = propiedadDAO.findPropiedadesByPrecioMayorQue(precio);

        if (propiedades != null) {
            return propiedades;
        } else {
            return new ArrayList<Propiedad>();
        }

    }

    private void validarDatos(@Valid PropiedadDTO propiedadDTODTO, BindingResult bindingResult) throws DatosNoValidosException {
        if (bindingResult.hasErrors()) {
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        }
    }

}
