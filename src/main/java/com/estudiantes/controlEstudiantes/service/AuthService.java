package com.estudiantes.controlEstudiantes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.estudiantes.controlEstudiantes.dto.LoginRequestDTO;
import com.estudiantes.controlEstudiantes.dto.LoginResponseDTO;
import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.exception.ResourceNotFoundException;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;
import com.estudiantes.controlEstudiantes.security.JwtUtils;
import com.estudiantes.controlEstudiantes.security.UserDetailsImpl;

@Service
public class AuthService {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    EstudianteRepository estudianteRepository;
    
    @Autowired
    JwtUtils jwtUtils;
    
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        // Verificar que el usuario existe y está activo
        Estudiante estudiante = estudianteRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + loginRequest.getEmail()));
        
        if (!estudiante.getActivo()) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + loginRequest.getEmail());
        }
        
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generar JWT
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // Obtener detalles del usuario
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Calcular tiempo de expiración
        Long expiration = System.currentTimeMillis() + jwtUtils.getJwtExpiration();
        
        return new LoginResponseDTO(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                estudiante.getNombreCompleto(),
                userDetails.getRol(),
                expiration
        );
    }
}
