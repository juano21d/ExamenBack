package com.estudiantes.controlEstudiantes.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;

public class UserDetailsImpl implements UserDetails {
    
    private Long id;
    private String email;
    private String password;
    private RolEstudiante rol;
    private boolean activo;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserDetailsImpl(Long id, String email, String password, RolEstudiante rol, 
                          boolean activo, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.authorities = authorities;
    }
    
    public static UserDetailsImpl build(Estudiante estudiante) {
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + estudiante.getRol().name())
        );
        
        return new UserDetailsImpl(
            estudiante.getId(),
            estudiante.getEmail(),
            estudiante.getPassword(),
            estudiante.getRol(),
            estudiante.getActivo(),
            authorities
        );
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Long getId() {
        return id;
    }
    
    public RolEstudiante getRol() {
        return rol;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return activo;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return activo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
