package com.estudiantes.controlEstudiantes.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class EstudianteRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 8, max = 20, message = "La cédula debe tener entre 8 y 20 caracteres")
    private String cedula;
    
    @NotBlank(message = "La carrera es obligatoria")
    @Size(min = 3, max = 100, message = "La carrera debe tener entre 3 y 100 caracteres")
    private String carrera;
    
    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser mínimo 1")
    @Max(value = 12, message = "El semestre debe ser máximo 12")
    private Integer semestre;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate fechaNacimiento;
    
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    // Constructores
    public EstudianteRequestDTO() {}
    
    public EstudianteRequestDTO(String nombre, String apellido, String email, String cedula, 
                               String carrera, Integer semestre, LocalDate fechaNacimiento, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cedula = cedula;
        this.carrera = carrera;
        this.semestre = semestre;
        this.fechaNacimiento = fechaNacimiento;
        this.password = password;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
