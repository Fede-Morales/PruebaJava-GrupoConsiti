package com.prueba.pruebaJava.security.service;

import com.prueba.pruebaJava.security.repository.UsuarioRepository;
import com.prueba.pruebaJava.security.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombreUsuario("Test");
        usuario.setEmail("test@email.com");
        usuario.setNombre("test1");
        usuario.setPassword("test123");

    }

    @Test
    void getByNombreUsuario() {

    }

    @Test
    void existsByNombreUsuario() {
        when(usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())).thenReturn(true);
        boolean exists = usuarioService.existsByNombreUsuario(usuario.getNombreUsuario());
        assertTrue(exists);
    }

    @Test
    void existsByEmail() {
    }

    @Test
    void save() {
        usuarioService.save(usuario);
        verify(usuarioRepository, times(1)).save(usuario);

    }
}