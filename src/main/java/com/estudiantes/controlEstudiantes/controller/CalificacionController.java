package com.estudiantes.controlEstudiantes.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.estudiantes.controlEstudiantes.dto.CalificacionRequestDTO;
import com.estudiantes.controlEstudiantes.dto.CalificacionResponseDTO;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.security.UserDetailsImpl;
import com.estudiantes.controlEstudiantes.service.CalificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "Calificaciones", description = "API para gestión de calificaciones")
@SecurityRequirement(name = "Bearer Authentication")
public class CalificacionController {
    
    @Autowired
    private CalificacionService calificacionService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todas las calificaciones", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de calificaciones obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerTodasLasCalificaciones() {
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerTodasLasCalificaciones();
        return ResponseEntity.ok(calificaciones);
    }
    
    @PostMapping("/crear")
    @PreAuthorize("hasRole('PROFESOR')")
    @Operation(summary = "Crear una nueva calificación", description = "Solo accesible para profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Calificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CalificacionResponseDTO> crearCalificacion(
            @Valid @RequestBody CalificacionRequestDTO calificacionRequestDTO,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        CalificacionResponseDTO calificacionCreada = calificacionService.crearCalificacion(
                calificacionRequestDTO, userDetails.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionCreada);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    @Operation(summary = "Actualizar calificación", 
               description = "Solo el profesor que asignó la calificación puede modificarla")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificación actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CalificacionResponseDTO> actualizarCalificacion(
            @Parameter(description = "ID de la calificación") @PathVariable Long id,
            @Valid @RequestBody CalificacionRequestDTO calificacionRequestDTO,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar permisos
        if (!calificacionService.puedeModificarCalificacion(userDetails.getEmail(), id, userDetails.getRol())) {
            throw new ResourceNotFoundException("Calificación no encontrada con ID: " + id);
        }
        
        CalificacionResponseDTO calificacionActualizada = calificacionService.actualizarCalificacion(
                id, calificacionRequestDTO, userDetails.getEmail());
        return ResponseEntity.ok(calificacionActualizada);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMIN')")
    @Operation(summary = "Eliminar calificación", 
               description = "Solo el profesor que asignó la calificación o un admin puede eliminarla")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Calificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<Void> eliminarCalificacion(
            @Parameter(description = "ID de la calificación") @PathVariable Long id,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar permisos
        if (!calificacionService.puedeModificarCalificacion(userDetails.getEmail(), id, userDetails.getRol())) {
            throw new ResourceNotFoundException("Calificación no encontrada con ID: " + id);
        }
        
        calificacionService.eliminarCalificacion(id, userDetails.getEmail());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener calificaciones de un estudiante", 
               description = "Los estudiantes solo pueden ver sus calificaciones, profesores y admins pueden ver todas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesPorEstudiante(
            @Parameter(description = "ID del estudiante") @PathVariable Long estudianteId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los estudiantes solo pueden ver sus propias calificaciones
        if (userDetails.getRol() == RolEstudiante.ESTUDIANTE && !userDetails.getId().equals(estudianteId)) {
            throw new ResourceNotFoundException("No tiene permisos para ver estas calificaciones");
        }
        
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesPorEstudiante(estudianteId);
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/materia/{materiaId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Obtener calificaciones de una materia", 
               description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesPorMateria(
            @Parameter(description = "ID de la materia") @PathVariable Long materiaId) {
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesPorMateria(materiaId);
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/profesor/{profesorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Obtener calificaciones asignadas por un profesor", 
               description = "Los profesores solo pueden ver las que han asignado, los admins todas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesPorProfesor(
            @Parameter(description = "ID del profesor") @PathVariable Long profesorId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los profesores solo pueden ver las calificaciones que han asignado
        if (userDetails.getRol() == RolEstudiante.PROFESOR && !userDetails.getId().equals(profesorId)) {
            throw new ResourceNotFoundException("No tiene permisos para ver estas calificaciones");
        }
        
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesPorProfesor(profesorId);
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    @Operation(summary = "Obtener calificaciones de un estudiante en una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesEstudianteEnMateria(
            @Parameter(description = "ID del estudiante") @PathVariable Long estudianteId,
            @Parameter(description = "ID de la materia") @PathVariable Long materiaId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los estudiantes solo pueden ver sus propias calificaciones
        if (userDetails.getRol() == RolEstudiante.ESTUDIANTE && !userDetails.getId().equals(estudianteId)) {
            throw new ResourceNotFoundException("No tiene permisos para ver estas calificaciones");
        }
        
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesEstudianteEnMateria(estudianteId, materiaId);
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/promedio/estudiante/{estudianteId}")
    @Operation(summary = "Calcular promedio general de un estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promedio calculado exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<BigDecimal> calcularPromedioGeneralEstudiante(
            @Parameter(description = "ID del estudiante") @PathVariable Long estudianteId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los estudiantes solo pueden ver su propio promedio
        if (userDetails.getRol() == RolEstudiante.ESTUDIANTE && !userDetails.getId().equals(estudianteId)) {
            throw new ResourceNotFoundException("No tiene permisos para ver este promedio");
        }
        
        BigDecimal promedio = calificacionService.calcularPromedioGeneralEstudiante(estudianteId);
        return ResponseEntity.ok(promedio);
    }
    
    @GetMapping("/promedio/estudiante/{estudianteId}/materia/{materiaId}")
    @Operation(summary = "Calcular promedio de un estudiante en una materia específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promedio calculado exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<BigDecimal> calcularPromedioEstudianteEnMateria(
            @Parameter(description = "ID del estudiante") @PathVariable Long estudianteId,
            @Parameter(description = "ID de la materia") @PathVariable Long materiaId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los estudiantes solo pueden ver su propio promedio
        if (userDetails.getRol() == RolEstudiante.ESTUDIANTE && !userDetails.getId().equals(estudianteId)) {
            throw new ResourceNotFoundException("No tiene permisos para ver este promedio");
        }
        
        BigDecimal promedio = calificacionService.calcularPromedioEstudianteEnMateria(estudianteId, materiaId);
        return ResponseEntity.ok(promedio);
    }
    
    @GetMapping("/promedio/materia/{materiaId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Calcular promedio general de una materia", 
               description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promedio calculado exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<BigDecimal> calcularPromedioMateria(
            @Parameter(description = "ID de la materia") @PathVariable Long materiaId) {
        BigDecimal promedio = calificacionService.calcularPromedioMateria(materiaId);
        return ResponseEntity.ok(promedio);
    }
    
    @GetMapping("/mis-calificaciones")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    @Operation(summary = "Obtener calificaciones del estudiante autenticado", 
               description = "Permite al estudiante ver sus calificaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerMisCalificaciones(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesPorEstudiante(userDetails.getId());
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/calificaciones-asignadas")
    @PreAuthorize("hasRole('PROFESOR')")
    @Operation(summary = "Obtener calificaciones asignadas por el profesor autenticado", 
               description = "Permite al profesor ver las calificaciones que ha asignado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificaciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesAsignadas(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<CalificacionResponseDTO> calificaciones = calificacionService.obtenerCalificacionesPorProfesor(userDetails.getId());
        return ResponseEntity.ok(calificaciones);
    }
    
    @GetMapping("/debug-auth")
    @Operation(summary = "Endpoint de debug para verificar autenticación")
    public ResponseEntity<Map<String, Object>> debugAuth(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of(
                "authenticated", false,
                "message", "No hay autenticación"
            ));
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "id", userDetails.getId(),
            "email", userDetails.getEmail(),
            "rol", userDetails.getRol().name(),
            "authorities", userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList(),
            "hasRoleEstudiante", userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ESTUDIANTE"))
        ));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener calificación por ID", 
               description = "Los estudiantes solo pueden ver sus calificaciones, los profesores las que han asignado, los admins todas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CalificacionResponseDTO> obtenerCalificacionPorId(
            @Parameter(description = "ID de la calificación") @PathVariable Long id,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar permisos
        if (!calificacionService.puedeVerCalificacion(userDetails.getEmail(), id, userDetails.getRol())) {
            throw new ResourceNotFoundException("Calificación no encontrada con ID: " + id);
        }
        
        CalificacionResponseDTO calificacion = calificacionService.obtenerCalificacionPorId(id);
        return ResponseEntity.ok(calificacion);
    }
}
