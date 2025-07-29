package com.estudiantes.controlEstudiantes.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estudiantes.controlEstudiantes.dto.LoginRequestDTO;
import com.estudiantes.controlEstudiantes.dto.LoginResponseDTO;
import com.estudiantes.controlEstudiantes.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {
    
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    
    @Autowired
    AuthService authService;
    
    @PostMapping({"/login", "/signin"})
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario (admin o estudiante) iniciar sesión")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            logger.info("=== INICIO LOGIN ===");
            logger.info("Email recibido: " + loginRequest.getEmail());
            logger.info("Password recibido: " + (loginRequest.getPassword() != null ? "***" : "NULL"));
            
            // Verificar que el servicio no sea null
            if (authService == null) {
                logger.severe("AuthService es NULL!");
                throw new RuntimeException("AuthService no disponible");
            }
            
            logger.info("Llamando a authService...");
            LoginResponseDTO response = authService.authenticateUser(loginRequest);
            logger.info("Login exitoso para: " + loginRequest.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("=== ERROR EN LOGIN ===");
            logger.severe("Error para usuario: " + loginRequest.getEmail());
            logger.severe("Tipo de error: " + e.getClass().getName());
            logger.severe("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @PostMapping("/test-signin")
    public ResponseEntity<Map<String, Object>> testSignin() {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "Test endpoint básico funcionando",
                "timestamp", java.time.LocalDateTime.now(),
                "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now(),
                "status", "error"
            ));
        }
    }
    
    @PostMapping("/test-signin-with-body")
    public ResponseEntity<Map<String, Object>> testSigninWithBody(@RequestBody(required = false) String rawBody) {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "Recibido: " + (rawBody != null ? rawBody : "sin body"),
                "timestamp", java.time.LocalDateTime.now(),
                "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now(),
                "status", "error"
            ));
        }
    }
    
    @GetMapping("/test-get")
    public ResponseEntity<Map<String, Object>> testGet() {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "GET endpoint funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now(),
                "status", "success",
                "server", "Spring Boot funcionando"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error: " + e.getMessage(),
                "stackTrace", e.getClass().getName(),
                "timestamp", java.time.LocalDateTime.now(),
                "status", "error"
            ));
        }
    }
    
    @PostMapping("/simple-test")
    public ResponseEntity<Map<String, Object>> simpleTest(@RequestBody Map<String, Object> body) {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "Endpoint simple funcionando",
                "received", body,
                "timestamp", java.time.LocalDateTime.now(),
                "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error: " + e.getMessage(),
                "stackTrace", e.getClass().getName(),
                "timestamp", java.time.LocalDateTime.now(),
                "status", "error"
            ));
        }
    }
}
