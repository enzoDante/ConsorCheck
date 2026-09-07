package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.DadosConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.DadosConsorcioResponseDTO;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.DadosConsorcioMapper;
import com.credito.ConsorCheck.model.DadosConsorcio;
import com.credito.ConsorCheck.repository.DadosConsorcioRepository;
import com.credito.ConsorCheck.validation.UserVerify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DadosConsorcioService {
    private final DadosConsorcioRepository dadosConsorcioRepository;
    private final DadosConsorcioMapper dadosConsorcioMapper;
    private final UserVerify userVerify;

    public DadosConsorcioService(DadosConsorcioMapper dadosConsorcioMapper, DadosConsorcioRepository dadosConsorcioRepository, UserVerify userVerify){
        this.dadosConsorcioMapper = dadosConsorcioMapper;
        this.dadosConsorcioRepository = dadosConsorcioRepository;
        this.userVerify = userVerify;
    }

    @Transactional
    public DadosConsorcioResponseDTO criar(DadosConsorcioRequestDTO dto){
        DadosConsorcio dadosConsorcio = dadosConsorcioMapper.toEntity(dto);
        return dadosConsorcioMapper.toDTO(dadosConsorcioRepository.save(dadosConsorcio));
    }

    @Transactional
    public DadosConsorcioResponseDTO update(Long id, DadosConsorcioRequestDTO dto){
        userVerify.sameUser(dto.getIdUsuario());
        DadosConsorcio dadosConsorcio = dadosConsorcioRepository.findById(id)
                .orElseThrow(() -> new SQLException("Consorcio não encontrado"));

        dadosConsorcioMapper.updateEntityFromDto(dto, dadosConsorcio);

        dadosConsorcio.setDataAtualizacao(LocalDateTime.now());

        return dadosConsorcioMapper.toDTO(dadosConsorcio);
    }
    @Transactional
    public void desativarAtivarConsorcio(Long id){
        DadosConsorcio dadosConsorcio = dadosConsorcioRepository.findById(id)
                .orElseThrow(() -> new SQLException("Consorcio não encontrado"));
        userVerify.sameUser(dadosConsorcio.getUsuario().getId());

        dadosConsorcio.setAtivo(!dadosConsorcio.isAtivo());
        dadosConsorcio.setDataAtualizacao(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<DadosConsorcioResponseDTO> getAll(){
        List<DadosConsorcio> dadosConsorcios = dadosConsorcioRepository.findAll();
        return dadosConsorcios.stream().map(dadosConsorcioMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<DadosConsorcioResponseDTO> getAllByEmpresa(Long idEmpresa){
        List<DadosConsorcio> dadosConsorcios = dadosConsorcioRepository.findByUsuarioId(idEmpresa);
        return dadosConsorcios.stream().map(dadosConsorcioMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DadosConsorcioResponseDTO getConsorcio(Long id){
        return dadosConsorcioMapper.toDTO(dadosConsorcioRepository.findById(id)
                .orElseThrow(() -> new SQLException("Consorcio não encontrado")));
    }
}
