package com.estudiantes.controlEstudiantes.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudiantes.controlEstudiantes.dto.CalificacionRequestDTO;
import com.estudiantes.controlEstudiantes.dto.CalificacionResponseDTO;
import com.estudiantes.controlEstudiantes.entity.Calificacion;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.Materia;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.repository.CalificacionRepository;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;
import com.estudiantes.controlEstudiantes.repository.MateriaRepository;

@Service
@Transactional
public class CalificacionService {
    
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    public List<CalificacionResponseDTO> obtenerTodasLasCalificaciones() {
        return calificacionRepository.findAllWithRelations()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public CalificacionResponseDTO obtenerCalificacionPorId(Long id) {
        Calificacion calificacion = calificacionRepository.findByIdWithRelations(id);
        if (calificacion == null) {
            throw new ResourceNotFoundException("Calificación no encontrada con ID: " + id);
        }
        return convertirAResponseDTO(calificacion);
    }
    
    public CalificacionResponseDTO crearCalificacion(CalificacionRequestDTO calificacionRequestDTO, String emailProfesor) {
        // Buscar al profesor que está creando la calificación
        Estudiante profesor = estudianteRepository.findByEmail(emailProfesor)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado"));
        
        // Verificar que el usuario sea profesor
        if (profesor.getRol() != RolEstudiante.PROFESOR) {
            throw new IllegalArgumentException("Solo los profesores pueden asignar calificaciones");
        }
        
        // Buscar al estudiante
        Estudiante estudiante = estudianteRepository.findById(calificacionRequestDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + calificacionRequestDTO.getEstudianteId()));
        
        // Buscar la materia
        Materia materia = materiaRepository.findById(calificacionRequestDTO.getMateriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + calificacionRequestDTO.getMateriaId()));
        
        // Verificar que el profesor dicte esa materia
        if (materia.getProfesor() == null || !materia.getProfesor().getId().equals(profesor.getId())) {
            throw new IllegalArgumentException("Solo puede calificar las materias que dicta");
        }
        
        Calificacion calificacion = convertirAEntity(calificacionRequestDTO);
        calificacion.setEstudiante(estudiante);
        calificacion.setMateria(materia);
        calificacion.setProfesor(profesor);
        
        Calificacion calificacionGuardada = calificacionRepository.save(calificacion);
        return convertirAResponseDTO(calificacionGuardada);
    }
    
    public CalificacionResponseDTO actualizarCalificacion(Long id, CalificacionRequestDTO calificacionRequestDTO, String emailProfesor) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con ID: " + id));
        
        // Verificar que el profesor que intenta actualizar sea el mismo que creó la calificación
        if (!calificacion.getProfesor().getEmail().equals(emailProfesor)) {
            throw new IllegalArgumentException("Solo puede modificar calificaciones que usted ha asignado");
        }
        
        // Actualizar campos
        calificacion.setNota(calificacionRequestDTO.getNota());
        calificacion.setTipoEvaluacion(calificacionRequestDTO.getTipoEvaluacion());
        calificacion.setObservaciones(calificacionRequestDTO.getObservaciones());
        
        Calificacion calificacionActualizada = calificacionRepository.save(calificacion);
        return convertirAResponseDTO(calificacionActualizada);
    }
    
    public void eliminarCalificacion(Long id, String emailProfesor) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con ID: " + id));
        
        // Verificar que el profesor que intenta eliminar sea el mismo que creó la calificación
        if (!calificacion.getProfesor().getEmail().equals(emailProfesor)) {
            throw new IllegalArgumentException("Solo puede eliminar calificaciones que usted ha asignado");
        }
        
        calificacionRepository.delete(calificacion);
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteIdWithRelations(estudianteId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesPorMateria(Long materiaId) {
        return calificacionRepository.findByMateriaIdWithRelations(materiaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesPorProfesor(Long profesorId) {
        return calificacionRepository.findByProfesorIdWithRelations(profesorId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesEstudianteEnMateria(Long estudianteId, Long materiaId) {
        return calificacionRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public BigDecimal calcularPromedioGeneralEstudiante(Long estudianteId) {
        BigDecimal promedio = calificacionRepository.calcularPromedioGeneralEstudiante(estudianteId);
        return promedio != null ? promedio : BigDecimal.ZERO;
    }
    
    public BigDecimal calcularPromedioEstudianteEnMateria(Long estudianteId, Long materiaId) {
        BigDecimal promedio = calificacionRepository.calcularPromedioEstudianteEnMateria(estudianteId, materiaId);
        return promedio != null ? promedio : BigDecimal.ZERO;
    }
    
    public BigDecimal calcularPromedioMateria(Long materiaId) {
        BigDecimal promedio = calificacionRepository.calcularPromedioMateria(materiaId);
        return promedio != null ? promedio : BigDecimal.ZERO;
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesAprobatorias() {
        return calificacionRepository.findCalificacionesAprobatorias()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<CalificacionResponseDTO> obtenerCalificacionesReprobatorias() {
        return calificacionRepository.findCalificacionesReprobatorias()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public boolean puedeVerCalificacion(String emailUsuario, Long idCalificacion, RolEstudiante rolUsuario) {
        Calificacion calificacion = calificacionRepository.findById(idCalificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con ID: " + idCalificacion));
        
        // Los admins pueden ver cualquier calificación
        if (rolUsuario == RolEstudiante.ADMIN) {
            return true;
        }
        
        // Los profesores pueden ver las calificaciones que han asignado
        if (rolUsuario == RolEstudiante.PROFESOR && calificacion.getProfesor().getEmail().equals(emailUsuario)) {
            return true;
        }
        
        // Los estudiantes solo pueden ver sus propias calificaciones
        return rolUsuario == RolEstudiante.ESTUDIANTE && calificacion.getEstudiante().getEmail().equals(emailUsuario);
    }
    
    public boolean puedeModificarCalificacion(String emailUsuario, Long idCalificacion, RolEstudiante rolUsuario) {
        // Solo los admins y los profesores que asignaron la calificación pueden modificarla
        if (rolUsuario == RolEstudiante.ADMIN) {
            return true;
        }
        
        if (rolUsuario == RolEstudiante.PROFESOR) {
            Calificacion calificacion = calificacionRepository.findById(idCalificacion)
                    .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con ID: " + idCalificacion));
            return calificacion.getProfesor().getEmail().equals(emailUsuario);
        }
        
        return false;
    }
    
    // Métodos de conversión
    private CalificacionResponseDTO convertirAResponseDTO(Calificacion calificacion) {
        CalificacionResponseDTO dto = new CalificacionResponseDTO();
        dto.setId(calificacion.getId());
        
        // Información del estudiante
        if (calificacion.getEstudiante() != null) {
            dto.setEstudianteId(calificacion.getEstudiante().getId());
            String nombreCompleto = calificacion.getEstudiante().getNombreCompleto();
            dto.setNombreEstudiante(nombreCompleto != null ? nombreCompleto : "Estudiante ID: " + calificacion.getEstudiante().getId());
            dto.setCedulaEstudiante(calificacion.getEstudiante().getCedula());
        } else {
            dto.setNombreEstudiante("N/A");
        }
        
        // Información de la materia
        if (calificacion.getMateria() != null) {
            dto.setMateriaId(calificacion.getMateria().getId());
            dto.setNombreMateria(calificacion.getMateria().getNombre());
            dto.setCodigoMateria(calificacion.getMateria().getCodigo());
        } else {
            dto.setNombreMateria("N/A");
            dto.setCodigoMateria("N/A");
        }
        
        // Información del profesor
        if (calificacion.getProfesor() != null) {
            dto.setProfesorId(calificacion.getProfesor().getId());
            String nombreProfesor = calificacion.getProfesor().getNombreCompleto();
            dto.setNombreProfesor(nombreProfesor != null ? nombreProfesor : "Profesor ID: " + calificacion.getProfesor().getId());
        } else {
            dto.setNombreProfesor("N/A");
        }
        
        dto.setNota(calificacion.getNota());
        dto.setTipoEvaluacion(calificacion.getTipoEvaluacion());
        dto.setObservaciones(calificacion.getObservaciones());
        dto.setFechaCreacion(calificacion.getFechaCreacion());
        dto.setFechaActualizacion(calificacion.getFechaActualizacion());
        dto.setNotaConFormato(calificacion.getNotaConFormato());
        dto.setEsAprobatoria(calificacion.esAprobatoria());
        return dto;
    }
    
    private Calificacion convertirAEntity(CalificacionRequestDTO dto) {
        Calificacion calificacion = new Calificacion();
        calificacion.setNota(dto.getNota());
        calificacion.setTipoEvaluacion(dto.getTipoEvaluacion());
        calificacion.setObservaciones(dto.getObservaciones());
        return calificacion;
    }
}
