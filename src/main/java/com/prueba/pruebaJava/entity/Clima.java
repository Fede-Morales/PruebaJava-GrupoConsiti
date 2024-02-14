package com.prueba.pruebaJava.entity;



import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clima {


    private String ciudad;
    private String descripcion;
    private double temperatura;


}

