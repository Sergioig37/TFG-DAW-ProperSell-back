package com.example.start.service;

import com.example.start.dao.AlertaDAO;
import com.example.start.dao.UsuarioDAO;
import com.example.start.entity.Alerta;
import com.example.start.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AlertaService {

    @Autowired
    AlertaDAO alertaDAO;

    @Autowired
    UsuarioDAO usuarioDAO;


    public void borrarAlerta(Long id){
        Optional<Alerta> alertaOptional = alertaDAO.findById(id);

        if (alertaOptional.isPresent()) {

            Set<Usuario> usuarios = alertaOptional.get().getUsuarios();

            for(Usuario usuario: usuarios){

                Set<Alerta> alertasUsuario = usuario.getAlertas();

                alertasUsuario.remove(alertaOptional.get());

                usuario.setAlertas(alertasUsuario);

                usuarioDAO.save(usuario);

            }

            usuarios.removeAll(alertaOptional.get().getUsuarios());
            alertaOptional.get().setUsuarios(usuarios);


            alertaDAO.delete(alertaOptional.get());

        }
    }

    public List<Alerta> encontrarAlertasPopulars(){


        List<Alerta> alertas = (List<Alerta>) alertaDAO.findAll();

        List<Alerta> alertasEncontradas = new ArrayList<>();


        for(Alerta alerta: alertas){
            if(alerta.getUsuarios().size()>1){
                alertasEncontradas.add(alerta);
            }
        }

        return alertasEncontradas;

    }

    public List<Alerta> getAlertasMasLargas(Long size){

        List<Alerta> alertas = (List<Alerta>) alertaDAO.findAll();

        List<Alerta> alertasEncontradas = new ArrayList<>();

        for(Alerta alerta: alertas){
            if(alerta.getDescripcion().length()>size){
                alertasEncontradas.add(alerta);
            }
        }

        return alertasEncontradas;
    }




}
