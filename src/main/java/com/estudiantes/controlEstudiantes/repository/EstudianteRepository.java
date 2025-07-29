package com.estudiantes.controlEstudiantes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Buscar por email (para autenticación)
    Optional<Estudiante> findByEmail(String email);
    
    // Buscar por cédula
    Optional<Estudiante> findByCedula(String cedula);
    
    // Verificar si existe por email
    boolean existsByEmail(String email);
    
    // Verificar si existe por cédula
    boolean existsByCedula(String cedula);
    
    // Buscar por rol
    List<Estudiante> findByRol(RolEstudiante rol);
    
    // Buscar estudiantes activos
    List<Estudiante> findByActivoTrue();
    
    // Buscar por carrera
    List<Estudiante> findByCarrera(String carrera);
    
    // Buscar por semestre
    List<Estudiante> findBySemestre(Integer semestre);
    
    // Buscar por carrera y semestre
    List<Estudiante> findByCarreraAndSemestre(String carrera, Integer semestre);
    
    // Buscar por nombre completo (ignorando mayúsculas)
    @Query("SELECT e FROM Estudiante e WHERE LOWER(CONCAT(e.nombre, ' ', e.apellido)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))")
    List<Estudiante> findByNombreCompletoContainingIgnoreCase(@Param("nombreCompleto") String nombreCompleto);
    
    // Buscar estudiantes activos por rol
    List<Estudiante> findByRolAndActivoTrue(RolEstudiante rol);
    
    // Contar estudiantes por carrera
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.carrera = :carrera AND e.activo = true")
    Long countByCarreraAndActivoTrue(@Param("carrera") String carrera);
    
    // Contar estudiantes por semestre
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.semestre = :semestre AND e.activo = true")
    Long countBySemestreAndActivoTrue(@Param("semestre") Integer semestre);
}
