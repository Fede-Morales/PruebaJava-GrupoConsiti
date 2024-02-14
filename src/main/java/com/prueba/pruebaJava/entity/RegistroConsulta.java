package com.prueba.pruebaJava.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;


@Getter
@Setter
@Entity
public class RegistroConsulta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int usuario_id;

    private String urlAccedida;

    @Lob
    private String respuestaClima;



}
