package com.prueba.pruebaJava.security.service;

import com.prueba.pruebaJava.security.repository.ConsultaRepository;
import com.prueba.pruebaJava.security.entity.Consulta;
import com.prueba.pruebaJava.security.enums.ConsultaNombre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ConsultaService {

    @Autowired
    ConsultaRepository consultaRepository;

    public Optional<Consulta> getByConsultaNombre(ConsultaNombre consultaNombre){
        return consultaRepository.findByConsultaNombre(consultaNombre);
    }

    public void save(Consulta consulta){
        consultaRepository.save(consulta);
    }
}
