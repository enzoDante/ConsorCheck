package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.UsuarioMapper;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.validation.CnpjVerify;
import com.credito.ConsorCheck.validation.CpfVerify;
import jakarta.persistence.PersistenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        try{
            if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()) throw new InvalidDataException("Email existente!", List.of());
            if(!CnpjVerify.isCnpjValido(dto.getDocumento()) && !CpfVerify.isValid(dto.getDocumento()))
                throw new InvalidDataException("Documento inválido, verifique se os digitos estão corretos", List.of());
            if(usuarioRepository.existsByDocumento(dto.getDocumento())) throw new BusinessException("Documento existente!");

            Usuario newUser = usuarioMapper.toEntity(dto);
            newUser.setRole(CnpjVerify.isCnpjValido(dto.getDocumento()) ? Role.EMPRESA : Role.CLIENTE);
            newUser.setSenha(passwordEncoder.encode(dto.getSenha()));
            return usuarioMapper.toDTO(usuarioRepository.save(newUser));
        }catch (DataIntegrityViolationException e){
            throw new SQLException("Erro de integridade no banco de dados, tente mais tarde");
        }
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> getAll(Pageable pageable){
        Page<Usuario> users = usuarioRepository.findAll(pageable);
        return users.map(usuarioMapper::toDTO);
        /*List<UsuarioResponseDTO> usersDto = new ArrayList<>();
        for(Usuario i : users){
            usersDto.add(usuarioMapper.toDTO(i));
        }
        return usersDto;*/
    }
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getById(Long id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new SQLException("Usuario não encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto){
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new SQLException("Usuário inexistente"));
        usuarioMapper.updateEntityFromDto(dto, user);

        return usuarioMapper.toDTO(user);
    }

    @Transactional
    public void inactiveUser(Long id){
        try{
            Usuario user = usuarioRepository.findByIdAndAtivo(id, true)
                    .orElseThrow(() -> new SQLException("Usuário inexistente ou ja desativado"));
            user.setAtivo(false);
            usuarioRepository.save(user);
        }catch (PersistenceException e){
            throw new SQLException("Erro de persistência de dados");
        }
    }
}
