package com.estudiantes.controlEstudiantes.dto;

import com.estudiantes.controlEstudiantes.entity.RolEstudiante;

public class LoginResponseDTO {
    
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String email;
    private String nombreCompleto;
    private RolEstudiante rol;
    private Long expiracion;
    
    // Constructores
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(String token, Long id, String email, String nombreCompleto, 
                           RolEstudiante rol, Long expiracion) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.expiracion = expiracion;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public RolEstudiante getRol() {
        return rol;
    }
    
    public void setRol(RolEstudiante rol) {
        this.rol = rol;
    }
    
    public Long getExpiracion() {
        return expiracion;
    }
    
    public void setExpiracion(Long expiracion) {
        this.expiracion = expiracion;
    }
}
