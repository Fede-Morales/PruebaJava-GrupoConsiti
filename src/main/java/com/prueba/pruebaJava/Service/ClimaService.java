package com.prueba.pruebaJava.Service;

import com.prueba.pruebaJava.Repository.RegistroConsultaRepository;
import com.prueba.pruebaJava.Response.AireDatos;
import com.prueba.pruebaJava.Response.AireMain;
import com.prueba.pruebaJava.entity.Aire;
import com.prueba.pruebaJava.entity.Clima;
import com.prueba.pruebaJava.entity.RegistroConsulta;
import com.prueba.pruebaJava.security.repository.UsuarioRepository;
import com.prueba.pruebaJava.Response.AireResponse;
import com.prueba.pruebaJava.Response.ClimaResponse;
import com.prueba.pruebaJava.security.entity.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClimaService {


    @Autowired
    private RegistroConsultaRepository registroConsultaRepository;

    @Value("${api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    private final UsuarioRepository usuarioRepository;

    private final String CLIMA_URL = "https://api.openweathermap.org/data/2.5/weather?q={ciudad}&appid={apiKey}&units=metric";
    private final String CLIMA_DIAS_URL = "https://api.openweathermap.org/data/2.5/forecast?q={ciudad}&appid={apiKey}&units=metric";
    private final String CALIDAD_AIRE = "http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={apiKey}&units=metric";

    Clima clima = new Clima();
    Aire aire = new Aire();
    List<Clima> pronostico = new ArrayList<>();



    public ClimaService(RestTemplate restTemplate, UsuarioRepository usuarioRepository) {
        this.restTemplate = restTemplate;
        this.usuarioRepository = usuarioRepository;
    }


    @Cacheable("climas")
    public Clima getClima(String ciudad){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        int usuarioId = usuario.getId();

        String apiUrl = CLIMA_URL.replace("{ciudad}", ciudad).replace("{apiKey}", apiKey);
        clima.setCiudad(ciudad);

        ClimaResponse response = restTemplate.getForObject(apiUrl, ClimaResponse.class);

        clima.setDescripcion(response.getWeather()[0].getDescription());
        clima.setTemperatura(response.getMain().getTemp());

        String climaString = clima.toString();

        guardarConsulta(apiUrl, climaString, usuarioId);

        return clima;
    }

    @Cacheable("pronostico")
    public List<Clima> getPronostico(String ciudad) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        int usuarioId = usuario.getId();

        String apiUrl = CLIMA_DIAS_URL.replace("{ciudad}", ciudad).replace("{apiKey}", apiKey);
        ClimaResponseWrapper responseWrapper = restTemplate.getForObject(apiUrl, ClimaResponseWrapper.class);
        List<ClimaResponse> responses = responseWrapper.getList();


        for (int i = 0; i < responses.size(); i++) {
            ClimaResponse response = responses.get(i);
            clima = new Clima();
            clima.setCiudad(ciudad);

            if (response.getWeather().length > 0) {
                clima.setDescripcion(response.getWeather()[0].getDescription());
            }
            clima.setTemperatura(response.getMain().getTemp());

            System.out.println("Temperatura");
            System.out.println(clima);
            pronostico.add(clima);
            String climaString = clima.toString();

            guardarConsulta(apiUrl, climaString, usuarioId);
        }

        return pronostico;
    }

    @Cacheable("aire")
    public Aire getCalidadAire(double lon, double lat){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        int usuarioId = usuario.getId();

        String apiUrl = CALIDAD_AIRE
                .replace("{lat}", String.valueOf(lat))
                .replace("{lon}", String.valueOf(lon))
                .replace("{apiKey}", apiKey);
        System.out.println(apiUrl);
        AireResponse response = restTemplate.getForObject(apiUrl, AireResponse.class);

        aire.setLatitud(String.valueOf(lat));
        aire.setLongitud(String.valueOf(lon));
        AireDatos aireDatos = response.getList()[0];
        AireMain aireMain = aireDatos.getMain();
        aire.setCalidad(String.valueOf(aireMain.getAqi()));


        String aireString = aire.toString();

        guardarConsulta(apiUrl, aireString, usuarioId);
        return aire;
    }

    private RegistroConsulta guardarConsulta(String urlAccedida, String climaString, int usuarioId) {

        RegistroConsulta registroConsulta = new RegistroConsulta();
        registroConsulta.setUrlAccedida(urlAccedida);
        registroConsulta.setUsuario_id(usuarioId);
        registroConsulta.setRespuestaClima(climaString);

        return registroConsultaRepository.save(registroConsulta);
    }


    @Getter
    @Setter
    private static class ClimaResponseWrapper {
        private List<ClimaResponse> list;

    }


}
