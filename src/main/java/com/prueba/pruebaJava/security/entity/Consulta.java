package com.prueba.pruebaJava.security.entity;

import com.prueba.pruebaJava.security.enums.ConsultaNombre;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta implements Serializable {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @NotNull
        @Enumerated(EnumType.STRING)
        private ConsultaNombre consultaNombre;

        public Consulta(@NotNull ConsultaNombre consultaNombre){
            this.consultaNombre = consultaNombre;
        }
}
