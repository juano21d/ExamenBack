package com.estudiantes.controlEstudiantes.dto;

import java.time.LocalDateTime;

public class MateriaResponseDTO {
    
    private Long id;
    private String nombre;
    private String codigo;
    private String descripcion;
    private Integer creditos;
    private Integer semestre;
    private String carrera;
    private String nombreProfesor;
    private Long profesorId;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String nombreCompleto;
    
    // Constructores
    public MateriaResponseDTO() {}
    
    public MateriaResponseDTO(Long id, String nombre, String codigo, String descripcion,
                             Integer creditos, Integer semestre, String carrera,
                             String nombreProfesor, Long profesorId, Boolean activa,
                             LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.semestre = semestre;
        this.carrera = carrera;
        this.nombreProfesor = nombreProfesor;
        this.profesorId = profesorId;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.nombreCompleto = codigo + " - " + nombre;
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
        this.nombreCompleto = (codigo != null ? codigo + " - " : "") + nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.nombreCompleto = codigo + " - " + (nombre != null ? nombre : "");
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getCreditos() {
        return creditos;
    }
    
    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
    
    public Integer getSemestre() {
        return semestre;
    }
    
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
    
    public String getCarrera() {
        return carrera;
    }
    
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    public String getNombreProfesor() {
        return nombreProfesor;
    }
    
    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
    
    public Long getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
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
