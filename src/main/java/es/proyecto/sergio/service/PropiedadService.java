package es.proyecto.sergio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.proyecto.sergio.dao.PropiedadDAO;
import es.proyecto.sergio.dao.UsuarioDAO;
import es.proyecto.sergio.dto.PropiedadDTO;
import es.proyecto.sergio.entity.Propiedad;
import es.proyecto.sergio.entity.Usuario;
import es.proyecto.sergio.exception.DatosNoValidosException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public  class PropiedadService {


    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    PropiedadDAO propiedadDAO;

    @Autowired
    private ModelMapper modelMapper;


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
            propiedad.setPrecio(propiedadDTO.getPrecio());
            propiedad.setSuperficie(propiedadDTO.getSuperficie());
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

    public ResponseEntity<?> actualizar(@Valid PropiedadDTO propiedad, Long id, BindingResult bindingResult, String username) {

        Optional<Propiedad> propiedadExiste = propiedadDAO.findById(id);

        try {
            this.validarDatos(propiedad, bindingResult);
            if (propiedadExiste.isPresent()) {

                if(propiedadExiste.get().getPropietario().getUsername().equals(username)){
                    propiedadExiste.get().setLocalizacion(propiedad.getLocalizacion());
                    propiedadExiste.get().setPrecio(Long.valueOf(propiedad.getPrecio()));
                    propiedadExiste.get().setSuperficie(Integer.valueOf(propiedad.getSuperficie()));
                    propiedadExiste.get().setTipo(propiedad.getTipo());

                    propiedadDAO.save(propiedadExiste.get());

                    return  ResponseEntity.status(HttpStatus.OK).body(null);
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (DatosNoValidosException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    public void habilitacionPropiedad(Long idPropiedad, boolean enabled) {

        Optional<Propiedad> propiedad = propiedadDAO.findById(idPropiedad);

        if (propiedad.isPresent()) {

            if (enabled==true) {
                propiedad.get().setHabilitado(true);
                System.out.println("Habilitando");

            } else if(enabled==false) {
                propiedad.get().setHabilitado(false);
                System.out.println("Deshabilitando");

            }
            System.out.println(propiedad.get());
            propiedadDAO.save(propiedad.get());
        }

    }

    public ResponseEntity<?> deletePropiedad(Long id, String username) {

        Optional<Propiedad> propiedadOptional = propiedadDAO.findById(id);

        if (propiedadOptional.isPresent()) {


            if(propiedadOptional.get().getPropietario().getUsername().equals(username)){
                System.out.println("ES IGUAL ");
                this.desenlazarPropietario(propiedadOptional.get());
                propiedadDAO.delete(propiedadOptional.get());
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public List<PropiedadDTO> propiedadesMasCarasQue(Long precio) {

        List<Propiedad> propiedades = propiedadDAO.findPropiedadesByPrecioMayorQue(precio);

        List<PropiedadDTO> propiedadDTOList = convertirAListaPropiedadDTO(propiedades);

        if (propiedadDTOList != null) {
            return propiedadDTOList;
        } else {
            return new ArrayList<PropiedadDTO>();
        }

    }

    private void validarDatos(@Valid PropiedadDTO propiedadDTODTO, BindingResult bindingResult) throws DatosNoValidosException {
        if (bindingResult.hasErrors()) {
            throw new DatosNoValidosException("Algunos campos del formulario no son válidos");
        }
    }

    public ResponseEntity<?> existe(Long id){

        Optional<Propiedad> propiedad = propiedadDAO.findById(id);

        if(propiedad.isPresent()){

            PropiedadDTO propiedadDTO = new PropiedadDTO();

            propiedadDTO = convertirAPropiedadDTO(propiedadDAO.findById(id).get());

            return ResponseEntity.status(HttpStatus.OK).body(propiedadDTO);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }


    public List<PropiedadDTO> convertirAListaPropiedadDTO(List<Propiedad> propiedades){

        List<PropiedadDTO> propiedadesDTO = new ArrayList<>();

        for(Propiedad propiedad: propiedades){

            PropiedadDTO propiedadDTO = modelMapper.map(propiedad, PropiedadDTO.class);


            propiedadesDTO.add(propiedadDTO);

        }


        return propiedadesDTO;


    }

    public PropiedadDTO convertirAPropiedadDTO(Propiedad propiedad){


        PropiedadDTO propiedadDTO = modelMapper.map(propiedad, PropiedadDTO.class);


        return propiedadDTO;
    }
}
