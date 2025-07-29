package com.estudiantes.controlEstudiantes;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.estudiantes.controlEstudiantes.dto.EstudianteRequestDTO;
import com.estudiantes.controlEstudiantes.dto.EstudianteResponseDTO;
import com.estudiantes.controlEstudiantes.service.EstudianteService;

@SpringBootTest
public class BackendTests {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testLoginPasswordEncoding() {
        String rawPassword = "test1234";
        String encoded = passwordEncoder.encode(rawPassword);
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    void testValidacionSemestre() {
        EstudianteRequestDTO dto = new EstudianteRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@test.com");
        dto.setCedula("12345678");
        dto.setCarrera("Ingenieria");
        dto.setSemestre(0); // inválido
        dto.setFechaNacimiento(LocalDate.of(2000,1,1));
        dto.setPassword("test1234");
        Assertions.assertThrows(Exception.class, () -> {
            estudianteService.crearEstudiante(dto);
        });
    }

    @Test
    void testCrudEstudiante() {
        EstudianteRequestDTO dto = new EstudianteRequestDTO();
        dto.setNombre("Ana");
        dto.setApellido("Lopez");
        dto.setEmail("ana@test.com");
        dto.setCedula("87654321");
        dto.setCarrera("Matematicas");
        dto.setSemestre(2);
        dto.setFechaNacimiento(LocalDate.of(2001,2,2));
        dto.setPassword("test1234");
        EstudianteResponseDTO creado = estudianteService.crearEstudiante(dto);
        Assertions.assertEquals("Ana", creado.getNombre());
        EstudianteResponseDTO obtenido = estudianteService.obtenerEstudiantePorEmail("ana@test.com");
        Assertions.assertEquals("Ana", obtenido.getNombre());
    }
    // The warning "testCrudEstudiante is never used" can be ignored for test methods annotated with @Test.
}
