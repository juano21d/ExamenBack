package com.estudiantes.controlEstudiantes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudiantes.controlEstudiantes.dto.MateriaRequestDTO;
import com.estudiantes.controlEstudiantes.dto.MateriaResponseDTO;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.Materia;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.DuplicateResourceException;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;
import com.estudiantes.controlEstudiantes.repository.MateriaRepository;

@Service
@Transactional
public class MateriaService {
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    public List<MateriaResponseDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findByActivaTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public MateriaResponseDTO obtenerMateriaPorId(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + id));
        return convertirAResponseDTO(materia);
    }
    
    public MateriaResponseDTO obtenerMateriaPorCodigo(String codigo) {
        Materia materia = materiaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con código: " + codigo));
        return convertirAResponseDTO(materia);
    }
    
    public MateriaResponseDTO crearMateria(MateriaRequestDTO materiaRequestDTO) {
        // Verificar que no exista una materia con el mismo código
        if (materiaRepository.existsByCodigo(materiaRequestDTO.getCodigo())) {
            throw new DuplicateResourceException("Ya existe una materia con el código: " + materiaRequestDTO.getCodigo());
        }
        
        Materia materia = convertirAEntity(materiaRequestDTO);
        
        // Asignar profesor si se proporcionó
        if (materiaRequestDTO.getProfesorId() != null) {
            Estudiante profesor = estudianteRepository.findById(materiaRequestDTO.getProfesorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con ID: " + materiaRequestDTO.getProfesorId()));
            
            // Verificar que el usuario sea efectivamente un profesor
            if (profesor.getRol() != RolEstudiante.PROFESOR) {
                throw new IllegalArgumentException("El usuario seleccionado no es un profesor");
            }
            
            materia.setProfesor(profesor);
        }
        
        Materia materiaGuardada = materiaRepository.save(materia);
        return convertirAResponseDTO(materiaGuardada);
    }
    
    public MateriaResponseDTO actualizarMateria(Long id, MateriaRequestDTO materiaRequestDTO) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + id));
        
        // Verificar que el código no esté en uso por otra materia
        if (!materia.getCodigo().equals(materiaRequestDTO.getCodigo()) && 
            materiaRepository.existsByCodigo(materiaRequestDTO.getCodigo())) {
            throw new DuplicateResourceException("Ya existe una materia con el código: " + materiaRequestDTO.getCodigo());
        }
        
        // Actualizar campos
        materia.setNombre(materiaRequestDTO.getNombre());
        materia.setCodigo(materiaRequestDTO.getCodigo());
        materia.setDescripcion(materiaRequestDTO.getDescripcion());
        materia.setCreditos(materiaRequestDTO.getCreditos());
        materia.setSemestre(materiaRequestDTO.getSemestre());
        materia.setCarrera(materiaRequestDTO.getCarrera());
        
        // Actualizar profesor si se proporcionó
        if (materiaRequestDTO.getProfesorId() != null) {
            Estudiante profesor = estudianteRepository.findById(materiaRequestDTO.getProfesorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con ID: " + materiaRequestDTO.getProfesorId()));
            
            if (profesor.getRol() != RolEstudiante.PROFESOR) {
                throw new IllegalArgumentException("El usuario seleccionado no es un profesor");
            }
            
            materia.setProfesor(profesor);
        } else {
            materia.setProfesor(null);
        }
        
        Materia materiaActualizada = materiaRepository.save(materia);
        return convertirAResponseDTO(materiaActualizada);
    }
    
    public void eliminarMateria(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + id));
        
        // Eliminación lógica
        materia.setActiva(false);
        materiaRepository.save(materia);
    }
    
    public List<MateriaResponseDTO> buscarPorCarrera(String carrera) {
        return materiaRepository.findByCarrera(carrera)
                .stream()
                .filter(Materia::getActiva)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MateriaResponseDTO> buscarPorSemestre(Integer semestre) {
        return materiaRepository.findBySemestre(semestre)
                .stream()
                .filter(Materia::getActiva)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MateriaResponseDTO> buscarPorCarreraYSemestre(String carrera, Integer semestre) {
        return materiaRepository.findByCarreraAndSemestre(carrera, semestre)
                .stream()
                .filter(Materia::getActiva)
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MateriaResponseDTO> buscarPorProfesor(Long profesorId) {
        return materiaRepository.findMateriasByProfesorId(profesorId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MateriaResponseDTO> buscarPorNombre(String nombre) {
        return materiaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<MateriaResponseDTO> buscarPorCodigo(String codigo) {
        return materiaRepository.findByCodigoContainingIgnoreCase(codigo)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    public boolean puedeEditarMateria(String emailUsuario, Long idMateria, RolEstudiante rolUsuario) {
        // Los admins pueden editar cualquier materia
        if (rolUsuario == RolEstudiante.ADMIN) {
            return true;
        }
        
        // Los profesores solo pueden editar las materias que dictan
        if (rolUsuario == RolEstudiante.PROFESOR) {
            Materia materia = materiaRepository.findById(idMateria)
                    .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con ID: " + idMateria));
            
            return materia.getProfesor() != null && materia.getProfesor().getEmail().equals(emailUsuario);
        }
        
        return false;
    }
    
    // Métodos de conversión
    private MateriaResponseDTO convertirAResponseDTO(Materia materia) {
        MateriaResponseDTO dto = new MateriaResponseDTO();
        dto.setId(materia.getId());
        dto.setNombre(materia.getNombre());
        dto.setCodigo(materia.getCodigo());
        dto.setDescripcion(materia.getDescripcion());
        dto.setCreditos(materia.getCreditos());
        dto.setSemestre(materia.getSemestre());
        dto.setCarrera(materia.getCarrera());
        dto.setActiva(materia.getActiva());
        dto.setFechaCreacion(materia.getFechaCreacion());
        dto.setFechaActualizacion(materia.getFechaActualizacion());
        dto.setNombreCompleto(materia.getNombreCompleto());
        
        if (materia.getProfesor() != null) {
            dto.setProfesorId(materia.getProfesor().getId());
            dto.setNombreProfesor(materia.getProfesor().getNombreCompleto());
        }
        
        return dto;
    }
    
    private Materia convertirAEntity(MateriaRequestDTO dto) {
        Materia materia = new Materia();
        materia.setNombre(dto.getNombre());
        materia.setCodigo(dto.getCodigo());
        materia.setDescripcion(dto.getDescripcion());
        materia.setCreditos(dto.getCreditos());
        materia.setSemestre(dto.getSemestre());
        materia.setCarrera(dto.getCarrera());
        return materia;
    }
}
