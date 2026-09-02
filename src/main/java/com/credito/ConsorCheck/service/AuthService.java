package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
}
