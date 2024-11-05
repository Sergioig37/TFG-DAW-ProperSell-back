package es.proyecto.sergio.dao;

import es.proyecto.sergio.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlertaDAO extends JpaRepository<Alerta, Long> {


}
