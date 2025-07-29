package com.estudiantes.controlEstudiantes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudiantes.controlEstudiantes.entity.Estudiante;
import com.estudiantes.controlEstudiantes.repository.EstudianteRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    EstudianteRepository estudianteRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        
        return UserDetailsImpl.build(estudiante);
    }
}
