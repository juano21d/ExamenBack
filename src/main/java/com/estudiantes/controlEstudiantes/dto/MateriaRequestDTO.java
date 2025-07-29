package com.estudiantes.controlEstudiantes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MateriaRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10, message = "El código no puede exceder 10 caracteres")
    private String codigo;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 10, message = "Los créditos no pueden exceder 10")
    private Integer creditos;
    
    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser al menos 1")
    @Max(value = 12, message = "El semestre no puede exceder 12")
    private Integer semestre;
    
    @Size(max = 100, message = "La carrera no puede exceder 100 caracteres")
    private String carrera;
    
    private Long profesorId;
    
    // Constructores
    public MateriaRequestDTO() {}
    
    public MateriaRequestDTO(String nombre, String codigo, String descripcion, 
                            Integer creditos, Integer semestre, String carrera, Long profesorId) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.semestre = semestre;
        this.carrera = carrera;
        this.profesorId = profesorId;
    }
    
    // Getters y Setters
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
    
    public Long getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }
}
