package com.prueba.pruebaJava.security.repository;

import com.prueba.pruebaJava.security.entity.Consulta;
import com.prueba.pruebaJava.security.enums.ConsultaNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    Optional<Consulta> findByConsultaNombre(ConsultaNombre consultaNombre);
}
