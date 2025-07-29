package com.estudiantes.controlEstudiantes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "calificaciones")
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Relación con Estudiante
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    // Relación con Materia
    @NotNull(message = "La materia es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
    
    // Relación con Profesor que asignó la calificación
    @NotNull(message = "El profesor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Estudiante profesor;
    
    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota debe ser al menos 0.0")
    @DecimalMax(value = "5.0", message = "La nota no puede exceder 5.0")
    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal nota;
    
    @Size(max = 20, message = "El tipo de evaluación no puede exceder 20 caracteres")
    @Column(length = 20)
    private String tipoEvaluacion; // Parcial, Final, Quiz, Taller, etc.
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Calificacion() {}
    
    public Calificacion(Estudiante estudiante, Materia materia, Estudiante profesor, 
                       BigDecimal nota, String tipoEvaluacion) {
        this.estudiante = estudiante;
        this.materia = materia;
        this.profesor = profesor;
        this.nota = nota;
        this.tipoEvaluacion = tipoEvaluacion;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Materia getMateria() {
        return materia;
    }
    
    public void setMateria(Materia materia) {
        this.materia = materia;
    }
    
    public Estudiante getProfesor() {
        return profesor;
    }
    
    public void setProfesor(Estudiante profesor) {
        this.profesor = profesor;
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
    
    // Métodos de utilidad
    public String getNotaConFormato() {
        return nota.toString() + "/5.0";
    }
    
    public boolean esAprobatoria() {
        return nota.compareTo(new BigDecimal("3.0")) >= 0;
    }
}
