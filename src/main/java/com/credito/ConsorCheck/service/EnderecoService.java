package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.EnderecoRequestDTO;
import com.credito.ConsorCheck.dto.EnderecoResponseDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.EnderecoMapper;
import com.credito.ConsorCheck.model.Endereco;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.EmpresaClienteRepository;
import com.credito.ConsorCheck.repository.EnderecoRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import jakarta.persistence.PersistenceException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {
    private final UsuarioRepository usuarioRepository;
    private final EmpresaClienteRepository empresaClienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;
    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioRepository usuarioRepository, EmpresaClienteRepository empresaClienteRepository, EnderecoMapper enderecoMapper){
        this.usuarioRepository = usuarioRepository;
        this.empresaClienteRepository = empresaClienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public EnderecoResponseDTO criar(EnderecoRequestDTO request, Long id_empresa){
        try{
            Endereco endereco = enderecoMapper.toEntity(request);
            if(id_empresa != null){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                boolean autenticado = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
                if(!autenticado)
                    throw new BusinessException("É necessário estar autenticado para cadastrar um cliente vinculado a uma empresa");
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                if(!userDetails.getId().equals(id_empresa) || userDetails.getRole() != Role.EMPRESA)
                    throw new BusinessException("Você só pode cadastrar clientes vinculados à sua própria empresa");
            }
            return enderecoMapper.toDTO(enderecoRepository.save(endereco));
        }catch (PersistenceException e){
            throw new SQLException("Erro ao inserir no banco de dados");
        }
    }

    @Transactional
    public EnderecoResponseDTO update(EnderecoRequestDTO dto, Long id){
        if(!id.equals(dto.getIdUsuario())) throw new InvalidDataException("Valores de id inconsistente", List.of());
        Endereco endereco = enderecoRepository.findById(id).orElseThrow(() -> new SQLException("Endereço não encontrado"));
        try{
            enderecoMapper.updateEntityFromDto(dto, endereco);
            return enderecoMapper.toDTO(endereco);
        }catch (PersistenceException e){
            throw new SQLException("Erro para atualizar o endereço");
        }
    }

    @Transactional(readOnly = true)
    public EnderecoResponseDTO get(Long id){
        Endereco endereco = enderecoRepository.findById(id).orElseThrow(() -> new SQLException("Endereço não encontrado"));
        return enderecoMapper.toDTO(endereco);
    }
    /* Criar o get com vários filtros e retornar junto o usuário (pelo menos o nome, obs: id do usuário ja está junto) */
}
