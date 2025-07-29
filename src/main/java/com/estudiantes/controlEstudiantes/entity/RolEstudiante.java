package com.estudiantes.controlEstudiantes.entity;

public enum RolEstudiante {
    ADMIN("Administrador"),
    ESTUDIANTE("Estudiante"),
    PROFESOR("Profesor");
    
    private final String descripcion;
    
    RolEstudiante(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
