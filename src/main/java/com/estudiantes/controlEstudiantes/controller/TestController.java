package com.estudiantes.controlEstudiantes.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "API funcionando correctamente",
            "timestamp", LocalDateTime.now()
        ));
    }
    
    @GetMapping("/echo")
    public ResponseEntity<Map<String, Object>> echoGet() {
        return ResponseEntity.ok(Map.of(
            "message", "Echo GET request received",
            "timestamp", LocalDateTime.now(),
            "status", "success"
        ));
    }
    
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echoPost(@RequestBody(required = false) String body) {
        String response = body != null ? "Echo: " + body : "Echo: POST request received (no body)";
        return ResponseEntity.ok(Map.of(
            "message", response,
            "timestamp", LocalDateTime.now(),
            "status", "success"
        ));
    }
    
    @PostMapping("/simple-login")
    public ResponseEntity<Map<String, Object>> simpleLogin(@RequestBody(required = false) String body) {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "Login simple recibido: " + (body != null ? body : "sin body"),
                "timestamp", LocalDateTime.now(),
                "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error en simple login: " + e.getMessage(),
                "timestamp", LocalDateTime.now(),
                "status", "error"
            ));
        }
    }
}
