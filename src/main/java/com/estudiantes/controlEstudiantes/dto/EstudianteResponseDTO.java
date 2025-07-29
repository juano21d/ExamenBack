package com.estudiantes.controlEstudiantes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.estudiantes.controlEstudiantes.entity.RolEstudiante;

public class EstudianteResponseDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String cedula;
    private String carrera;
    private Integer semestre;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;
    private RolEstudiante rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String nombreCompleto;
    
    // Constructores
    public EstudianteResponseDTO() {}
    
    public EstudianteResponseDTO(Long id, String nombre, String apellido, String email, String cedula,
                                String carrera, Integer semestre, LocalDate fechaNacimiento, String telefono,
                                String direccion, RolEstudiante rol, Boolean activo, LocalDateTime fechaCreacion,
                                LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cedula = cedula;
        this.carrera = carrera;
        this.semestre = semestre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.nombreCompleto = nombre + " " + apellido;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreCompleto = nombre + " " + (apellido != null ? apellido : "");
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
        this.nombreCompleto = (nombre != null ? nombre : "") + " " + apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getCarrera() {
        return carrera;
    }
    
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    public Integer getSemestre() {
        return semestre;
    }
    
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public RolEstudiante getRol() {
        return rol;
    }
    
    public void setRol(RolEstudiante rol) {
        this.rol = rol;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
