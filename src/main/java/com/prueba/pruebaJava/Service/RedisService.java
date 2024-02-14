package com.prueba.pruebaJava.Service;

import com.prueba.pruebaJava.Repository.RedisRepository;
import com.prueba.pruebaJava.entity.Clima;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

public class RedisService implements RedisRepository {

    private static final String KEY = "Clima";

    private RedisTemplate<String, Clima> redisTemplate;
    private HashOperations hashOperations;

    public RedisService(RedisTemplate<String, Clima> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, Clima> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void save(Clima clima) {
        hashOperations.put(KEY, UUID.randomUUID().toString(), clima);
    }

    @Override
    public void delete(Clima ciudad) {
        hashOperations.delete(KEY, ciudad);
    }
}
