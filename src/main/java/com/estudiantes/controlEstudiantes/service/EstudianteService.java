package com.estudiantes.controlEstudiantes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudiantes.controlEstudiantes.dto.EstudianteRequestDTO;
import com.estudiantes.controlEstudiantes.dto.EstudianteResponseDTO;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.DuplicateResourceException;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.repository.CalificacionRepository;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;
import com.estudiantes.controlEstudiantes.repository.MateriaRepository;

@Service
@Transactional
public class EstudianteService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    public List<EstudianteResponseDTO> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<EstudianteResponseDTO> obtenerProfesores() {
        return estudianteRepository.findByRol(RolEstudiante.PROFESOR)
                .stream()
                .filter(Estudiante::getActivo)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public EstudianteResponseDTO obtenerEstudiantePorId(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + id));
        
        if (!estudiante.getActivo()) {
            throw new ResourceNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        
        return convertirAResponseDTO(estudiante);
    }
    
    public EstudianteResponseDTO obtenerEstudiantePorEmail(String email) {
        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con email: " + email));
        
        if (!estudiante.getActivo()) {
            throw new ResourceNotFoundException("Estudiante no encontrado con email: " + email);
        }
        
        return convertirAResponseDTO(estudiante);
    }
    
    public EstudianteResponseDTO crearEstudiante(EstudianteRequestDTO estudianteRequestDTO) {
        // Validar que no exista el email
        if (estudianteRepository.existsByEmail(estudianteRequestDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un estudiante con el email: " + estudianteRequestDTO.getEmail());
        }
        
        // Validar que no exista la cédula
        if (estudianteRepository.existsByCedula(estudianteRequestDTO.getCedula())) {
            throw new DuplicateResourceException("Ya existe un estudiante con la cédula: " + estudianteRequestDTO.getCedula());
        }
        
        Estudiante estudiante = convertirAEntity(estudianteRequestDTO);
        estudiante.setPassword(passwordEncoder.encode(estudianteRequestDTO.getPassword()));
        estudiante.setRol(RolEstudiante.ESTUDIANTE);
        
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        return convertirAResponseDTO(estudianteGuardado);
    }
    
    public EstudianteResponseDTO actualizarEstudiante(Long id, EstudianteRequestDTO estudianteRequestDTO) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + id));
        
        if (!estudiante.getActivo()) {
            throw new ResourceNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        
        // Validar email único (solo si cambió)
        if (!estudiante.getEmail().equals(estudianteRequestDTO.getEmail()) && 
            estudianteRepository.existsByEmail(estudianteRequestDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un estudiante con el email: " + estudianteRequestDTO.getEmail());
        }
        
        // Validar cédula única (solo si cambió)
        if (!estudiante.getCedula().equals(estudianteRequestDTO.getCedula()) && 
            estudianteRepository.existsByCedula(estudianteRequestDTO.getCedula())) {
            throw new DuplicateResourceException("Ya existe un estudiante con la cédula: " + estudianteRequestDTO.getCedula());
        }
        
        // Actualizar campos
        estudiante.setNombre(estudianteRequestDTO.getNombre());
        estudiante.setApellido(estudianteRequestDTO.getApellido());
        estudiante.setEmail(estudianteRequestDTO.getEmail());
        estudiante.setCedula(estudianteRequestDTO.getCedula());
        estudiante.setCarrera(estudianteRequestDTO.getCarrera());
        estudiante.setSemestre(estudianteRequestDTO.getSemestre());
        estudiante.setFechaNacimiento(estudianteRequestDTO.getFechaNacimiento());
        estudiante.setTelefono(estudianteRequestDTO.getTelefono());
        estudiante.setDireccion(estudianteRequestDTO.getDireccion());
        
        // Solo actualizar contraseña si se proporciona
        if (estudianteRequestDTO.getPassword() != null && !estudianteRequestDTO.getPassword().trim().isEmpty()) {
            estudiante.setPassword(passwordEncoder.encode(estudianteRequestDTO.getPassword()));
        }
        
        Estudiante estudianteActualizado = estudianteRepository.save(estudiante);
        return convertirAResponseDTO(estudianteActualizado);
    }
    
    public void eliminarEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + id));
        
        // Eliminación lógica
        estudiante.setActivo(false);
        estudianteRepository.save(estudiante);
    }
    
    public List<EstudianteResponseDTO> buscarPorCarrera(String carrera) {
        return estudianteRepository.findByCarrera(carrera)
                .stream()
                .filter(Estudiante::getActivo)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<EstudianteResponseDTO> buscarPorSemestre(Integer semestre) {
        return estudianteRepository.findBySemestre(semestre)
                .stream()
                .filter(Estudiante::getActivo)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<EstudianteResponseDTO> buscarPorNombreCompleto(String nombreCompleto) {
        return estudianteRepository.findByNombreCompletoContainingIgnoreCase(nombreCompleto)
                .stream()
                .filter(Estudiante::getActivo)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public boolean puedeEditarEstudiante(String emailUsuario, Long idEstudiante, RolEstudiante rolUsuario) {
        // Los admins pueden editar cualquier estudiante
        if (rolUsuario == RolEstudiante.ADMIN) {
            return true;
        }
        
        // Los estudiantes solo pueden editar su propio perfil
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + idEstudiante));
        
        return estudiante.getEmail().equals(emailUsuario);
    }

    public List<EstudianteResponseDTO> obtenerEstudiantesDeMateria(Long materiaId, Long profesorId) {
        // Verificar que la materia existe y que el profesor tiene acceso a ella
        com.estudiantes.controlEstudiantes.entity.Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + materiaId));
        
        // Verificar que el profesor enseña esta materia
        if (!materia.getProfesor().getId().equals(profesorId)) {
            throw new ResourceNotFoundException("No tienes acceso a esta materia");
        }
        
        // Obtener estudiantes únicos que tienen calificaciones en esta materia
        return calificacionRepository.findByMateriaId(materiaId)
                .stream()
                .map(calificacion -> calificacion.getEstudiante())
                .distinct()
                .filter(Estudiante::getActivo)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversión
    private EstudianteResponseDTO convertirAResponseDTO(Estudiante estudiante) {
        return new EstudianteResponseDTO(
            estudiante.getId(),
            estudiante.getNombre(),
            estudiante.getApellido(),
            estudiante.getEmail(),
            estudiante.getCedula(),
            estudiante.getCarrera(),
            estudiante.getSemestre(),
            estudiante.getFechaNacimiento(),
            estudiante.getTelefono(),
            estudiante.getDireccion(),
            estudiante.getRol(),
            estudiante.getActivo(),
            estudiante.getFechaCreacion(),
            estudiante.getFechaActualizacion()
        );
    }
    
    private Estudiante convertirAEntity(EstudianteRequestDTO dto) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(dto.getNombre());
        estudiante.setApellido(dto.getApellido());
        estudiante.setEmail(dto.getEmail());
        estudiante.setCedula(dto.getCedula());
        estudiante.setCarrera(dto.getCarrera());
        estudiante.setSemestre(dto.getSemestre());
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setDireccion(dto.getDireccion());
        return estudiante;
    }
}
