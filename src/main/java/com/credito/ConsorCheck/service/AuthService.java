package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.AuthRequestDTO;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void login(AuthRequestDTO dto){
        Usuario user = usuarioRepository.findByNomeOrEmail(dto.getUsername(), dto.getUsername())
                .orElseThrow(() -> new SQLException("Usuário inválidos"));
        if(!passwordEncoder.matches(dto.getSenha(), user.getSenha()))
            throw new InvalidDataException("Senha incorreta", List.of());


    }
}
