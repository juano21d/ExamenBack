package com.estudiantes.controlEstudiantes.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CalificacionRequestDTO {
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;
    
    @NotNull(message = "El ID de la materia es obligatorio")
    private Long materiaId;
    
    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota debe ser al menos 0.0")
    @DecimalMax(value = "5.0", message = "La nota no puede exceder 5.0")
    private BigDecimal nota;
    
    @Size(max = 20, message = "El tipo de evaluación no puede exceder 20 caracteres")
    private String tipoEvaluacion;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    // Constructores
    public CalificacionRequestDTO() {}
    
    public CalificacionRequestDTO(Long estudianteId, Long materiaId, BigDecimal nota, 
                                 String tipoEvaluacion, String observaciones) {
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.nota = nota;
        this.tipoEvaluacion = tipoEvaluacion;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public Long getEstudianteId() {
        return estudianteId;
    }
    
    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }
    
    public Long getMateriaId() {
        return materiaId;
    }
    
    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }
    
    public BigDecimal getNota() {
        return nota;
    }
    
    public void setNota(BigDecimal nota) {
        this.nota = nota;
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
}
