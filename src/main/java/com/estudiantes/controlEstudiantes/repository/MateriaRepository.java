package com.estudiantes.controlEstudiantes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    
    // Buscar por código
    Optional<Materia> findByCodigo(String codigo);
    
    // Verificar si existe por código
    boolean existsByCodigo(String codigo);
    
    // Buscar materias activas
    List<Materia> findByActivaTrue();
    
    // Buscar por carrera
    List<Materia> findByCarrera(String carrera);
    
    // Buscar por semestre
    List<Materia> findBySemestre(Integer semestre);
    
    // Buscar por carrera y semestre
    List<Materia> findByCarreraAndSemestre(String carrera, Integer semestre);
    
    // Buscar por profesor
    List<Materia> findByProfesor(Estudiante profesor);
    
    // Buscar materias que dicta un profesor específico
    @Query("SELECT m FROM Materia m WHERE m.profesor.id = :profesorId AND m.activa = true")
    List<Materia> findMateriasByProfesorId(@Param("profesorId") Long profesorId);
    
    // Buscar por nombre que contenga
    @Query("SELECT m FROM Materia m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND m.activa = true")
    List<Materia> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar por código que contenga
    @Query("SELECT m FROM Materia m WHERE LOWER(m.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')) AND m.activa = true")
    List<Materia> findByCodigoContainingIgnoreCase(@Param("codigo") String codigo);
    
    // Contar materias por carrera
    @Query("SELECT COUNT(m) FROM Materia m WHERE m.carrera = :carrera AND m.activa = true")
    Long countByCarrera(@Param("carrera") String carrera);
    
    // Contar materias por profesor
    @Query("SELECT COUNT(m) FROM Materia m WHERE m.profesor.id = :profesorId AND m.activa = true")
    Long countByProfesorId(@Param("profesorId") Long profesorId);
}
