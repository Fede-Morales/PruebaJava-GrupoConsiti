package com.prueba.pruebaJava.Service;

import com.prueba.pruebaJava.Repository.RegistroConsultaRepository;
import com.prueba.pruebaJava.entity.RegistroConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistroConsultaService {

    @Autowired
    private RegistroConsultaRepository registroConsultaRepository;

    public void guardarRegistroConsulta(RegistroConsulta registroConsulta) {
        registroConsultaRepository.save(registroConsulta);
    }
}
