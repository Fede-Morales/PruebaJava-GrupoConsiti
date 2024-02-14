package com.prueba.pruebaJava.controller;

import com.prueba.pruebaJava.Service.ClimaService;
import com.prueba.pruebaJava.entity.Aire;
import com.prueba.pruebaJava.entity.Clima;
import com.prueba.pruebaJava.security.entity.Consulta;
import com.prueba.pruebaJava.security.enums.ConsultaNombre;
import com.prueba.pruebaJava.security.repository.UsuarioRepository;
import com.prueba.pruebaJava.security.service.ConsultaService;
import com.prueba.pruebaJava.security.entity.Usuario;
import com.prueba.pruebaJava.security.service.UsuarioService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
class ClimaController {


    @Value("${api.key}")
    private String apiKey;

    @Autowired
    ConsultaService consultaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ClimaService climaService;


    private final UsuarioRepository usuarioRepository;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public ClimaController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @PostConstruct
    public void init() {
        Bandwidth limit = Bandwidth.classic(3, Refill.of(3, Duration.ofHours(1)));
        buckets.put("default", Bucket4j.builder().addLimit(limit).build());
    }

    Clima clima = new Clima();
    Usuario nuevoUsuario = new Usuario();


    @GetMapping("/clima/{ciudad}")
    public Clima getClima(@PathVariable String ciudad) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = getBucketForClient(username);


        Set<Consulta> consulta = new HashSet<>();
        consulta.add(consultaService.getByConsultaNombre(ConsultaNombre.CLIMA).get());
        nuevoUsuario = usuario;

        try {
            if (bucket.tryConsume(1)) {

                clima = climaService.getClima(ciudad);

            }else{
                throw new RuntimeException("Se agoto la cantidad de consultas permitidas");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return clima;
    }


    @GetMapping("/pronostico/{ciudad}")
    public List<Clima> getPronosticoClima(@PathVariable String ciudad) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = getBucketForClient(username);

        Set<Consulta> consulta = new HashSet<>();
        consulta.add(consultaService.getByConsultaNombre(ConsultaNombre.PRONOSTICO).get());
        nuevoUsuario = usuario;


        List<Clima> pronostico = new ArrayList<>();


        try {
            if (bucket.tryConsume(1)) {

                pronostico = climaService.getPronostico(ciudad);

            }else{
            throw new RuntimeException("Se agoto la cantidad de consultas permitidas");
        }
        }catch(Exception e){
        System.out.println(e.getMessage());
    }

    return pronostico;
}





    @GetMapping("/calidadAire/{lon},{lat}")
    public Aire getCalidadAire(@PathVariable double lon, @PathVariable double lat) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = getBucketForClient(username);


        Set<Consulta> consulta = new HashSet<>();
        consulta.add(consultaService.getByConsultaNombre(ConsultaNombre.CALIDADAIRE).get());
        nuevoUsuario = usuario;

        Aire aire = new Aire();

        try {
            if (bucket.tryConsume(1)) {

                aire = climaService.getCalidadAire(lon, lat);

            }else{
                throw new RuntimeException("Se agoto la cantidad de consultas permitidas");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return aire;
    }


    private Bucket getBucketForClient(String username) {
        return buckets.computeIfAbsent(username, k -> Bucket4j.builder()
                .addLimit(Bandwidth.classic(3, Refill.of(3, Duration.ofHours(1))))
                .build());
    }

}


