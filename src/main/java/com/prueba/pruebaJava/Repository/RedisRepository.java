package com.prueba.pruebaJava.Repository;

import com.prueba.pruebaJava.entity.Clima;

import java.util.Map;

public interface RedisRepository {
    Map<String, Clima> findAll();
    void save(Clima clima);
    void delete(Clima ciudad);
}
