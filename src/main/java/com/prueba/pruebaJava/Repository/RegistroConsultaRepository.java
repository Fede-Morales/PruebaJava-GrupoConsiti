package com.prueba.pruebaJava.Repository;

import com.prueba.pruebaJava.entity.RegistroConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroConsultaRepository extends JpaRepository<RegistroConsulta, Long> {

    List<RegistroConsulta> findByUrlAccedida(String urlAccedida);












}
