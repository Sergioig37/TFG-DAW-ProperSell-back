package com.example.start.dao;

import com.example.start.entity.Alerta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertaDAO extends CrudRepository<Alerta, Long> {


}
