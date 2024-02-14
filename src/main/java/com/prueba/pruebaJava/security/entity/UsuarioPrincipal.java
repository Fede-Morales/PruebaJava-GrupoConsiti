package com.prueba.pruebaJava.security.entity;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class UsuarioPrincipal implements UserDetails{
    
    private String nombre;
    private String nombreUsuario;
    private String email;
    private String password;
            
    
    public UsuarioPrincipal(String nombre, String nombreUsuario, String email, String password){
      this.nombre = nombre;
      this.nombreUsuario = nombreUsuario;
      this.email = email;
      this.password = password;
    }
    
    public static UsuarioPrincipal build(Usuario usuario){
        return new UsuarioPrincipal(usuario.getNombre(), usuario.getNombreUsuario(), usuario.getEmail(), usuario.getPassword());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
