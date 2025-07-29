package com.estudiantes.controlEstudiantes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estudiantes.controlEstudiantes.dto.EstudianteRequestDTO;
import com.estudiantes.controlEstudiantes.dto.EstudianteResponseDTO;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.security.UserDetailsImpl;
import com.estudiantes.controlEstudiantes.service.EstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "Estudiantes", description = "API para gestión de estudiantes")
@SecurityRequirement(name = "Bearer Authentication")
public class EstudianteController {
    
    @Autowired
    private EstudianteService estudianteService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los estudiantes", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> obtenerTodosLosEstudiantes() {
        List<EstudianteResponseDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo estudiante", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Email o cédula ya existe"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EstudianteResponseDTO> crearEstudiante(
            @Valid @RequestBody EstudianteRequestDTO estudianteRequestDTO) {
        EstudianteResponseDTO nuevoEstudiante = estudianteService.crearEstudiante(estudianteRequestDTO);
        return new ResponseEntity<>(nuevoEstudiante, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID", 
               description = "Los estudiantes solo pueden ver su propio perfil, los admins pueden ver cualquiera")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorId(
            @Parameter(description = "ID del estudiante") @PathVariable Long id,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los estudiantes solo pueden ver su propio perfil
        if (userDetails.getRol() == RolEstudiante.ESTUDIANTE && !userDetails.getId().equals(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        
        EstudianteResponseDTO estudiante = estudianteService.obtenerEstudiantePorId(id);
        return ResponseEntity.ok(estudiante);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estudiante", 
               description = "Los estudiantes solo pueden actualizar su propio perfil, los admins pueden actualizar cualquiera")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "409", description = "Email o cédula ya existe"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EstudianteResponseDTO> actualizarEstudiante(
            @Parameter(description = "ID del estudiante") @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDTO estudianteRequestDTO,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar permisos
        if (!estudianteService.puedeEditarEstudiante(userDetails.getEmail(), id, userDetails.getRol())) {
            throw new ResourceNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        
        EstudianteResponseDTO estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteRequestDTO);
        return ResponseEntity.ok(estudianteActualizado);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar estudiante", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Estudiante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<Void> eliminarEstudiante(
            @Parameter(description = "ID del estudiante") @PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar/carrera/{carrera}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar estudiantes por carrera", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorCarrera(
            @Parameter(description = "Nombre de la carrera") @PathVariable String carrera) {
        List<EstudianteResponseDTO> estudiantes = estudianteService.buscarPorCarrera(carrera);
        return ResponseEntity.ok(estudiantes);
    }
    
    @GetMapping("/buscar/semestre/{semestre}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar estudiantes por semestre", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorSemestre(
            @Parameter(description = "Número del semestre") @PathVariable Integer semestre) {
        List<EstudianteResponseDTO> estudiantes = estudianteService.buscarPorSemestre(semestre);
        return ResponseEntity.ok(estudiantes);
    }
    
    @GetMapping("/buscar/nombre")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar estudiantes por nombre", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre completo o parcial") @RequestParam String nombre) {
        List<EstudianteResponseDTO> estudiantes = estudianteService.buscarPorNombreCompleto(nombre);
        return ResponseEntity.ok(estudiantes);
    }
    
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario autenticado", 
               description = "Permite al usuario ver su propio perfil")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EstudianteResponseDTO> obtenerPerfil(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        EstudianteResponseDTO estudiante = estudianteService.obtenerEstudiantePorEmail(userDetails.getEmail());
        return ResponseEntity.ok(estudiante);
    }
    
    @GetMapping("/profesores")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los profesores", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de profesores obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> obtenerTodosLosProfesores() {
        List<EstudianteResponseDTO> profesores = estudianteService.obtenerProfesores();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/materia/{materiaId}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMIN')")
    @Operation(summary = "Obtener estudiantes de una materia específica", 
               description = "Permite a los profesores ver los estudiantes inscritos en sus materias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    public ResponseEntity<List<EstudianteResponseDTO>> obtenerEstudiantesDeMateria(
            @Parameter(description = "ID de la materia") @PathVariable Long materiaId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar que el profesor tiene acceso a esta materia
        List<EstudianteResponseDTO> estudiantes = estudianteService.obtenerEstudiantesDeMateria(materiaId, userDetails.getId());
        return ResponseEntity.ok(estudiantes);
    }
}
