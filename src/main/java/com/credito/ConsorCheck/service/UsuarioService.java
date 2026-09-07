package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.UsuarioMapper;
import com.credito.ConsorCheck.model.EmpresaCliente;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.EmpresaClienteRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import com.credito.ConsorCheck.validation.CnpjVerify;
import com.credito.ConsorCheck.validation.CpfVerify;
import jakarta.persistence.PersistenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EmpresaClienteRepository empresaClienteRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaClienteRepository empresaClienteRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.empresaClienteRepository = empresaClienteRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto, Long id_empresa){
        try{
            if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()) throw new InvalidDataException("Email existente!", List.of());
            if(!CnpjVerify.isCnpjValido(dto.getDocumento()) && !CpfVerify.isValid(dto.getDocumento()))
                throw new InvalidDataException("Documento inválido, verifique se os digitos estão corretos", List.of());
            if(usuarioRepository.existsByDocumento(dto.getDocumento())) throw new BusinessException("Documento existente!");

            Usuario newUser = usuarioMapper.toEntity(dto);
            newUser.setRole(CnpjVerify.isCnpjValido(dto.getDocumento()) ? Role.EMPRESA : Role.CLIENTE);
            newUser.setSenha(passwordEncoder.encode(dto.getSenha()));
            newUser = usuarioRepository.save(newUser);

            if(id_empresa != null){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                boolean autenticado = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
                if(!autenticado)
                    throw new BusinessException("É necessário estar autenticado para cadastrar um cliente vinculado a uma empresa");
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                if(!userDetails.getId().equals(id_empresa) || userDetails.getRole() != Role.EMPRESA)
                    throw new BusinessException("Você só pode cadastrar clientes vinculados à sua própria empresa");
                Usuario empresa = usuarioRepository.findById(id_empresa)
                        .orElseThrow(() -> new SQLException("Empresa não encontrada"));
                EmpresaCliente vinculo = new EmpresaCliente();
                vinculo.setCliente(newUser);
                vinculo.setEmpresa(empresa);
                empresaClienteRepository.save(vinculo);
            }

            return usuarioMapper.toDTO(newUser);
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
