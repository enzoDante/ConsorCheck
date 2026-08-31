package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.mapper.UsuarioMapper;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto){
        Usuario newUser = usuarioMapper.toEntity(dto);
        newUser.setRole(Role.ADMIN);
        newUser.setSenha(passwordEncoder.encode(dto.getSenha()));
        return usuarioMapper.toDTO(usuarioRepository.save(newUser));
    }

    @Transactional
    public List<UsuarioResponseDTO> getAll(){
        /*List<UsuarioResponseDTO> users = usuarioMapper.toDTO(usuarioRepository.findAll());*/
        List<Usuario> users = usuarioRepository.findAll();
        List<UsuarioResponseDTO> usersDto = new ArrayList<>();
        for(Usuario i : users){
            usersDto.add(usuarioMapper.toDTO(i));
        }
        return usersDto;
    }
    @Transactional
    public UsuarioResponseDTO getById(Long id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("erro"));
    }
}
