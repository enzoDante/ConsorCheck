package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.EnderecoRequestDTO;
import com.credito.ConsorCheck.dto.EnderecoResponseDTO;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.EnderecoMapper;
import com.credito.ConsorCheck.model.Endereco;
import com.credito.ConsorCheck.repository.EnderecoRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;
    public EnderecoService(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper){
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public EnderecoResponseDTO criar(EnderecoRequestDTO request){
        try{
            Endereco endereco = enderecoMapper.toEntity(request);
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
