package com.estudiantes.controlEstudiantes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CalificacionResponseDTO {
    
    private Long id;
    private Long estudianteId;
    private String nombreEstudiante;
    private String cedulaEstudiante;
    private Long materiaId;
    private String nombreMateria;
    private String codigoMateria;
    private Long profesorId;
    private String nombreProfesor;
    private BigDecimal nota;
    private String tipoEvaluacion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String notaConFormato;
    private Boolean esAprobatoria;
    
    // Constructores
    public CalificacionResponseDTO() {}
    
    public CalificacionResponseDTO(Long id, Long estudianteId, String nombreEstudiante, 
                                  String cedulaEstudiante, Long materiaId, String nombreMateria,
                                  String codigoMateria, Long profesorId, String nombreProfesor,
                                  BigDecimal nota, String tipoEvaluacion, String observaciones,
                                  LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.cedulaEstudiante = cedulaEstudiante;
        this.materiaId = materiaId;
        this.nombreMateria = nombreMateria;
        this.codigoMateria = codigoMateria;
        this.profesorId = profesorId;
        this.nombreProfesor = nombreProfesor;
        this.nota = nota;
        this.tipoEvaluacion = tipoEvaluacion;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.notaConFormato = nota.toString() + "/5.0";
        this.esAprobatoria = nota.compareTo(new BigDecimal("3.0")) >= 0;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEstudianteId() {
        return estudianteId;
    }
    
    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }
    
    public String getNombreEstudiante() {
        return nombreEstudiante;
    }
    
    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }
    
    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }
    
    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }
    
    public Long getMateriaId() {
        return materiaId;
    }
    
    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }
    
    public String getNombreMateria() {
        return nombreMateria;
    }
    
    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }
    
    public String getCodigoMateria() {
        return codigoMateria;
    }
    
    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }
    
    public Long getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }
    
    public String getNombreProfesor() {
        return nombreProfesor;
    }
    
    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
    
    public BigDecimal getNota() {
        return nota;
    }
    
    public void setNota(BigDecimal nota) {
        this.nota = nota;
        this.notaConFormato = nota.toString() + "/5.0";
        this.esAprobatoria = nota.compareTo(new BigDecimal("3.0")) >= 0;
    }
    
    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }
    
    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    
    public String getNotaConFormato() {
        return notaConFormato;
    }
    
    public void setNotaConFormato(String notaConFormato) {
        this.notaConFormato = notaConFormato;
    }
    
    public Boolean getEsAprobatoria() {
        return esAprobatoria;
    }
    
    public void setEsAprobatoria(Boolean esAprobatoria) {
        this.esAprobatoria = esAprobatoria;
    }
}
