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

import com.estudiantes.controlEstudiantes.dto.MateriaRequestDTO;
import com.estudiantes.controlEstudiantes.dto.MateriaResponseDTO;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.security.UserDetailsImpl;
import com.estudiantes.controlEstudiantes.service.MateriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "API para gestión de materias")
@SecurityRequirement(name = "Bearer Authentication")
public class MateriaController {
    
    @Autowired
    private MateriaService materiaService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Obtener todas las materias", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> obtenerTodasLasMaterias() {
        List<MateriaResponseDTO> materias = materiaService.obtenerTodasLasMaterias();
        return ResponseEntity.ok(materias);
    }
    
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear una nueva materia", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Materia creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Código de materia ya existe"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<MateriaResponseDTO> crearMateria(
            @Valid @RequestBody MateriaRequestDTO materiaRequestDTO) {
        MateriaResponseDTO materiaCreada = materiaService.crearMateria(materiaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaCreada);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener materia por ID", 
               description = "Los profesores solo pueden ver las materias que dictan, los admins pueden ver cualquiera")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia encontrada"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<MateriaResponseDTO> obtenerMateriaPorId(
            @Parameter(description = "ID de la materia") @PathVariable Long id,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Los profesores solo pueden ver las materias que dictan
        if (userDetails.getRol() == RolEstudiante.PROFESOR) {
            if (!materiaService.puedeEditarMateria(userDetails.getEmail(), id, userDetails.getRol())) {
                throw new ResourceNotFoundException("Materia no encontrada con ID: " + id);
            }
        }
        
        MateriaResponseDTO materia = materiaService.obtenerMateriaPorId(id);
        return ResponseEntity.ok(materia);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar materia", 
               description = "Los profesores solo pueden actualizar las materias que dictan, los admins pueden actualizar cualquiera")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materia actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "409", description = "Código de materia ya existe"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<MateriaResponseDTO> actualizarMateria(
            @Parameter(description = "ID de la materia") @PathVariable Long id,
            @Valid @RequestBody MateriaRequestDTO materiaRequestDTO,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Verificar permisos
        if (!materiaService.puedeEditarMateria(userDetails.getEmail(), id, userDetails.getRol())) {
            throw new ResourceNotFoundException("Materia no encontrada con ID: " + id);
        }
        
        MateriaResponseDTO materiaActualizada = materiaService.actualizarMateria(id, materiaRequestDTO);
        return ResponseEntity.ok(materiaActualizada);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar materia", description = "Solo accesible para administradores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Materia eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<Void> eliminarMateria(
            @Parameter(description = "ID de la materia") @PathVariable Long id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar/carrera/{carrera}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Buscar materias por carrera", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> buscarPorCarrera(
            @Parameter(description = "Nombre de la carrera") @PathVariable String carrera) {
        List<MateriaResponseDTO> materias = materiaService.buscarPorCarrera(carrera);
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/buscar/semestre/{semestre}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Buscar materias por semestre", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> buscarPorSemestre(
            @Parameter(description = "Número del semestre") @PathVariable Integer semestre) {
        List<MateriaResponseDTO> materias = materiaService.buscarPorSemestre(semestre);
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/buscar/profesor/{profesorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Buscar materias por profesor", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> buscarPorProfesor(
            @Parameter(description = "ID del profesor") @PathVariable Long profesorId) {
        List<MateriaResponseDTO> materias = materiaService.buscarPorProfesor(profesorId);
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/buscar/nombre")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Buscar materias por nombre", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre completo o parcial") @RequestParam String nombre) {
        List<MateriaResponseDTO> materias = materiaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/buscar/codigo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @Operation(summary = "Buscar materias por código", description = "Solo accesible para administradores y profesores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> buscarPorCodigo(
            @Parameter(description = "Código completo o parcial") @RequestParam String codigo) {
        List<MateriaResponseDTO> materias = materiaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(materias);
    }
    
    @GetMapping("/mis-materias")
    @PreAuthorize("hasRole('PROFESOR')")
    @Operation(summary = "Obtener materias del profesor autenticado", 
               description = "Permite al profesor ver las materias que dicta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materias obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<MateriaResponseDTO>> obtenerMisMaterias(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<MateriaResponseDTO> materias = materiaService.buscarPorProfesor(userDetails.getId());
        return ResponseEntity.ok(materias);
    }
}
