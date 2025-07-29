package com.estudiantes.controlEstudiantes.entity;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "materias")
public class Materia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10, message = "El código no puede exceder 10 caracteres")
    @Column(nullable = false, length = 10, unique = true)
    private String codigo;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 10, message = "Los créditos no pueden exceder 10")
    @Column(nullable = false)
    private Integer creditos;
    
    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser al menos 1")
    @Max(value = 12, message = "El semestre no puede exceder 12")
    @Column(nullable = false)
    private Integer semestre;
    
    @Size(max = 100, message = "La carrera no puede exceder 100 caracteres")
    @Column(length = 100)
    private String carrera;
    
    // Relación con Profesor (muchas materias pueden ser dictadas por un profesor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Estudiante profesor;
    
    @Column(nullable = false)
    private Boolean activa = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Materia() {}
    
    public Materia(String nombre, String codigo, String descripcion, Integer creditos, 
                   Integer semestre, String carrera) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.semestre = semestre;
        this.carrera = carrera;
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
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
    public Estudiante getProfesor() {
        return profesor;
    }
    
    public void setProfesor(Estudiante profesor) {
        this.profesor = profesor;
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
        return this.codigo + " - " + this.nombre;
    }
}
