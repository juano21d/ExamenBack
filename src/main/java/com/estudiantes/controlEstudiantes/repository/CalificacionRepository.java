package com.estudiantes.controlEstudiantes.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estudiantes.controlEstudiantes.entity.Calificacion;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.Materia;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    
    // Consulta para obtener todas las calificaciones con entidades relacionadas cargadas
    @Query("SELECT c FROM Calificacion c " +
           "LEFT JOIN FETCH c.estudiante " +
           "LEFT JOIN FETCH c.materia " +
           "LEFT JOIN FETCH c.profesor")
    List<Calificacion> findAllWithRelations();
    
    // Buscar calificaciones por estudiante con entidades relacionadas
    @Query("SELECT c FROM Calificacion c " +
           "LEFT JOIN FETCH c.estudiante " +
           "LEFT JOIN FETCH c.materia " +
           "LEFT JOIN FETCH c.profesor " +
           "WHERE c.estudiante.id = :estudianteId")
    List<Calificacion> findByEstudianteIdWithRelations(@Param("estudianteId") Long estudianteId);
    
    // Buscar calificaciones por materia con entidades relacionadas
    @Query("SELECT c FROM Calificacion c " +
           "LEFT JOIN FETCH c.estudiante " +
           "LEFT JOIN FETCH c.materia " +
           "LEFT JOIN FETCH c.profesor " +
           "WHERE c.materia.id = :materiaId")
    List<Calificacion> findByMateriaIdWithRelations(@Param("materiaId") Long materiaId);
    
    // Buscar calificaciones por profesor con entidades relacionadas
    @Query("SELECT c FROM Calificacion c " +
           "LEFT JOIN FETCH c.estudiante " +
           "LEFT JOIN FETCH c.materia " +
           "LEFT JOIN FETCH c.profesor " +
           "WHERE c.profesor.id = :profesorId")
    List<Calificacion> findByProfesorIdWithRelations(@Param("profesorId") Long profesorId);
    
    // Buscar calificacion por ID con entidades relacionadas
    @Query("SELECT c FROM Calificacion c " +
           "LEFT JOIN FETCH c.estudiante " +
           "LEFT JOIN FETCH c.materia " +
           "LEFT JOIN FETCH c.profesor " +
           "WHERE c.id = :id")
    Calificacion findByIdWithRelations(@Param("id") Long id);
    
    // Buscar calificaciones por estudiante
    List<Calificacion> findByEstudiante(Estudiante estudiante);
    
    // Buscar calificaciones por estudiante ID
    @Query("SELECT c FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    List<Calificacion> findByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    // Buscar calificaciones por materia
    List<Calificacion> findByMateria(Materia materia);
    
    // Buscar calificaciones por materia ID
    @Query("SELECT c FROM Calificacion c WHERE c.materia.id = :materiaId")
    List<Calificacion> findByMateriaId(@Param("materiaId") Long materiaId);
    
    // Buscar calificaciones por profesor
    List<Calificacion> findByProfesor(Estudiante profesor);
    
    // Buscar calificaciones por profesor ID
    @Query("SELECT c FROM Calificacion c WHERE c.profesor.id = :profesorId")
    List<Calificacion> findByProfesorId(@Param("profesorId") Long profesorId);
    
    // Buscar calificaciones de un estudiante en una materia específica
    @Query("SELECT c FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.materia.id = :materiaId")
    List<Calificacion> findByEstudianteIdAndMateriaId(@Param("estudianteId") Long estudianteId, 
                                                      @Param("materiaId") Long materiaId);
    
    // Buscar calificaciones por tipo de evaluación
    List<Calificacion> findByTipoEvaluacion(String tipoEvaluacion);
    
    // Buscar calificaciones aprobatorias (nota >= 3.0)
    @Query("SELECT c FROM Calificacion c WHERE c.nota >= 3.0")
    List<Calificacion> findCalificacionesAprobatorias();
    
    // Buscar calificaciones reprobatorias (nota < 3.0)
    @Query("SELECT c FROM Calificacion c WHERE c.nota < 3.0")
    List<Calificacion> findCalificacionesReprobatorias();
    
    // Calcular promedio de un estudiante en todas las materias
    @Query("SELECT AVG(c.nota) FROM Calificacion c WHERE c.estudiante.id = :estudianteId")
    BigDecimal calcularPromedioGeneralEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Calcular promedio de un estudiante en una materia específica
    @Query("SELECT AVG(c.nota) FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.materia.id = :materiaId")
    BigDecimal calcularPromedioEstudianteEnMateria(@Param("estudianteId") Long estudianteId, 
                                                   @Param("materiaId") Long materiaId);
    
    // Calcular promedio general de una materia
    @Query("SELECT AVG(c.nota) FROM Calificacion c WHERE c.materia.id = :materiaId")
    BigDecimal calcularPromedioMateria(@Param("materiaId") Long materiaId);
    
    // Contar calificaciones aprobatorias de un estudiante
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.nota >= 3.0")
    Long contarCalificacionesAprobatoriasEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Contar calificaciones reprobatorias de un estudiante
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.estudiante.id = :estudianteId AND c.nota < 3.0")
    Long contarCalificacionesReprobatoriasEstudiante(@Param("estudianteId") Long estudianteId);
    
    // Buscar mejores calificaciones (top 10)
    @Query("SELECT c FROM Calificacion c ORDER BY c.nota DESC")
    List<Calificacion> findTop10ByOrderByNotaDesc();
    
    // Buscar calificaciones por rango de notas
    @Query("SELECT c FROM Calificacion c WHERE c.nota BETWEEN :notaMin AND :notaMax")
    List<Calificacion> findByNotaBetween(@Param("notaMin") BigDecimal notaMin, 
                                        @Param("notaMax") BigDecimal notaMax);
}