package com.prueba.pruebaJava.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClimaResponse {

    private ClimaInfo[] weather;
    private MainInfo main;

}