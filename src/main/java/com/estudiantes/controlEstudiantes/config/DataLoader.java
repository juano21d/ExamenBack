package com.estudiantes.controlEstudiantes.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.estudiantes.controlEstudiantes.entity.Calificacion;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.entity.Materia;
import com.estudiantes.controlEstudiantes.entity.RolEstudiante;
import com.estudiantes.controlEstudiantes.repository.CalificacionRepository;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;
import com.estudiantes.controlEstudiantes.repository.MateriaRepository;

@Component
public class DataLoader implements CommandLineRunner {
    
    private static final Logger logger = Logger.getLogger(DataLoader.class.getName());
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear administrador por defecto si no existe
        if (!estudianteRepository.existsByEmail("admin@sistema.com")) {
            Estudiante admin = new Estudiante();
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEmail("admin@sistema.com");
            admin.setCedula("00000000");
            admin.setCarrera("Administración");
            admin.setSemestre(1);
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setTelefono("123456789");
            admin.setDireccion("Dirección Admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(RolEstudiante.ADMIN);
            admin.setActivo(true);
            
            estudianteRepository.save(admin);
            logger.info("Administrador por defecto creado: admin@sistema.com / admin123");
        }
        
        // Crear algunos estudiantes de prueba si no existen
        if (!estudianteRepository.existsByEmail("estudiante1@test.com")) {
            Estudiante estudiante1 = new Estudiante();
            estudiante1.setNombre("Juan");
            estudiante1.setApellido("Pérez");
            estudiante1.setEmail("estudiante1@test.com");
            estudiante1.setCedula("12345678");
            estudiante1.setCarrera("Ingeniería de Sistemas");
            estudiante1.setSemestre(5);
            estudiante1.setFechaNacimiento(LocalDate.of(2000, 3, 15));
            estudiante1.setTelefono("987654321");
            estudiante1.setDireccion("Calle 123 #45-67");
            estudiante1.setPassword(passwordEncoder.encode("123456"));
            estudiante1.setRol(RolEstudiante.ESTUDIANTE);
            estudiante1.setActivo(true);
            
            estudianteRepository.save(estudiante1);
            logger.info("Estudiante de prueba creado: estudiante1@test.com / 123456");
        }
        
        if (!estudianteRepository.existsByEmail("estudiante2@test.com")) {
            Estudiante estudiante2 = new Estudiante();
            estudiante2.setNombre("María");
            estudiante2.setApellido("González");
            estudiante2.setEmail("estudiante2@test.com");
            estudiante2.setCedula("87654321");
            estudiante2.setCarrera("Administración de Empresas");
            estudiante2.setSemestre(3);
            estudiante2.setFechaNacimiento(LocalDate.of(2001, 7, 22));
            estudiante2.setTelefono("456789123");
            estudiante2.setDireccion("Avenida 89 #12-34");
            estudiante2.setPassword(passwordEncoder.encode("123456"));
            estudiante2.setRol(RolEstudiante.ESTUDIANTE);
            estudiante2.setActivo(true);
            
            estudianteRepository.save(estudiante2);
            logger.info("Estudiante de prueba creado: estudiante2@test.com / 123456");
        }
        
        logger.info("Inicialización de datos completada");
        
        // Crear profesores de prueba si no existen
        Estudiante profesor1;
        if (!estudianteRepository.existsByEmail("profesor1@test.com")) {
            profesor1 = new Estudiante();
            profesor1.setNombre("Dr. Carlos");
            profesor1.setApellido("Rodríguez");
            profesor1.setEmail("profesor1@test.com");
            profesor1.setCedula("11111111");
            profesor1.setCarrera("Ingeniería");
            profesor1.setSemestre(1);
            profesor1.setFechaNacimiento(LocalDate.of(1980, 5, 10));
            profesor1.setTelefono("321654987");
            profesor1.setDireccion("Calle Profesores #10-20");
            profesor1.setPassword(passwordEncoder.encode("prof123"));
            profesor1.setRol(RolEstudiante.PROFESOR);
            profesor1.setActivo(true);
            
            profesor1 = estudianteRepository.save(profesor1);
            logger.info("Profesor de prueba creado: profesor1@test.com / prof123");
        } else {
            profesor1 = estudianteRepository.findByEmail("profesor1@test.com").orElse(null);
        }
        
        Estudiante profesor2;
        if (!estudianteRepository.existsByEmail("profesor2@test.com")) {
            profesor2 = new Estudiante();
            profesor2.setNombre("Dra. Ana");
            profesor2.setApellido("Martínez");
            profesor2.setEmail("profesor2@test.com");
            profesor2.setCedula("22222222");
            profesor2.setCarrera("Administración");
            profesor2.setSemestre(1);
            profesor2.setFechaNacimiento(LocalDate.of(1985, 8, 15));
            profesor2.setTelefono("654987321");
            profesor2.setDireccion("Avenida Académicos #30-40");
            profesor2.setPassword(passwordEncoder.encode("prof123"));
            profesor2.setRol(RolEstudiante.PROFESOR);
            profesor2.setActivo(true);
            
            profesor2 = estudianteRepository.save(profesor2);
            logger.info("Profesor de prueba creado: profesor2@test.com / prof123");
        } else {
            profesor2 = estudianteRepository.findByEmail("profesor2@test.com").orElse(null);
        }
        
        // Crear materias de prueba si no existen
        Materia materia1;
        if (!materiaRepository.existsByCodigo("IS101")) {
            materia1 = new Materia();
            materia1.setNombre("Programación I");
            materia1.setCodigo("IS101");
            materia1.setDescripcion("Introducción a la programación con Java");
            materia1.setCreditos(4);
            materia1.setSemestre(1);
            materia1.setCarrera("Ingeniería de Sistemas");
            materia1.setProfesor(profesor1);
            materia1.setActiva(true);
            
            materia1 = materiaRepository.save(materia1);
            logger.info("Materia de prueba creada: IS101 - Programación I");
        } else {
            materia1 = materiaRepository.findByCodigo("IS101").orElse(null);
        }
        
        Materia materia2;
        if (!materiaRepository.existsByCodigo("IS201")) {
            materia2 = new Materia();
            materia2.setNombre("Base de Datos");
            materia2.setCodigo("IS201");
            materia2.setDescripcion("Diseño y gestión de bases de datos relacionales");
            materia2.setCreditos(3);
            materia2.setSemestre(4);
            materia2.setCarrera("Ingeniería de Sistemas");
            materia2.setProfesor(profesor1);
            materia2.setActiva(true);
            
            materia2 = materiaRepository.save(materia2);
            logger.info("Materia de prueba creada: IS201 - Base de Datos");
        } else {
            materia2 = materiaRepository.findByCodigo("IS201").orElse(null);
        }
        
        Materia materia3;
        if (!materiaRepository.existsByCodigo("AD101")) {
            materia3 = new Materia();
            materia3.setNombre("Administración General");
            materia3.setCodigo("AD101");
            materia3.setDescripcion("Fundamentos de la administración empresarial");
            materia3.setCreditos(3);
            materia3.setSemestre(1);
            materia3.setCarrera("Administración de Empresas");
            materia3.setProfesor(profesor2);
            materia3.setActiva(true);
            
            materia3 = materiaRepository.save(materia3);
            logger.info("Materia de prueba creada: AD101 - Administración General");
        } else {
            materia3 = materiaRepository.findByCodigo("AD101").orElse(null);
        }
        
        // Crear calificaciones de prueba si no existen
        Estudiante estudiante1 = estudianteRepository.findByEmail("estudiante1@test.com").orElse(null);
        Estudiante estudiante2 = estudianteRepository.findByEmail("estudiante2@test.com").orElse(null);
        
        if (estudiante1 != null && materia1 != null && profesor1 != null) {
            // Verificar si ya existe una calificación para este estudiante en esta materia
            if (calificacionRepository.findByEstudianteIdAndMateriaId(estudiante1.getId(), materia1.getId()).isEmpty()) {
                Calificacion calificacion1 = new Calificacion();
                calificacion1.setEstudiante(estudiante1);
                calificacion1.setMateria(materia1);
                calificacion1.setProfesor(profesor1);
                calificacion1.setNota(new BigDecimal("4.2"));
                calificacion1.setTipoEvaluacion("Parcial");
                calificacion1.setObservaciones("Excelente comprensión de los conceptos");
                
                calificacionRepository.save(calificacion1);
                logger.info("Calificación de prueba creada: Estudiante 1 - Programación I - 4.2");
            }
            
            if (calificacionRepository.findByEstudianteIdAndMateriaId(estudiante1.getId(), materia2.getId()).isEmpty()) {
                Calificacion calificacion2 = new Calificacion();
                calificacion2.setEstudiante(estudiante1);
                calificacion2.setMateria(materia2);
                calificacion2.setProfesor(profesor1);
                calificacion2.setNota(new BigDecimal("3.8"));
                calificacion2.setTipoEvaluacion("Final");
                calificacion2.setObservaciones("Buen manejo de consultas SQL");
                
                calificacionRepository.save(calificacion2);
                logger.info("Calificación de prueba creada: Estudiante 1 - Base de Datos - 3.8");
            }
        }
        
        if (estudiante2 != null && materia3 != null && profesor2 != null) {
            if (calificacionRepository.findByEstudianteIdAndMateriaId(estudiante2.getId(), materia3.getId()).isEmpty()) {
                Calificacion calificacion3 = new Calificacion();
                calificacion3.setEstudiante(estudiante2);
                calificacion3.setMateria(materia3);
                calificacion3.setProfesor(profesor2);
                calificacion3.setNota(new BigDecimal("4.5"));
                calificacion3.setTipoEvaluacion("Parcial");
                calificacion3.setObservaciones("Excelente participación en clase");
                
                calificacionRepository.save(calificacion3);
                logger.info("Calificación de prueba creada: Estudiante 2 - Administración General - 4.5");
            }
        }
        
        logger.info("Inicialización completa de datos completada - Incluye profesores, materias y calificaciones");
    }
}
