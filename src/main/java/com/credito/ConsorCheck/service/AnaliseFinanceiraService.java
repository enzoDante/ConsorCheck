package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.AnaliseFinanceiraRequestDTO;
import com.credito.ConsorCheck.dto.AnaliseFinanceiraResponseDTO;
import com.credito.ConsorCheck.mapper.AnaliseFinanceiraMapper;
import com.credito.ConsorCheck.model.AnaliseFinanceira;
import com.credito.ConsorCheck.repository.AnaliseFinanceiraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnaliseFinanceiraService {
    private final AnaliseFinanceiraRepository repository;
    private final AnaliseFinanceiraMapper mapper;
    public AnaliseFinanceiraService(AnaliseFinanceiraRepository repository, AnaliseFinanceiraMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public AnaliseFinanceiraResponseDTO criarAnalise(AnaliseFinanceiraRequestDTO request){
        AnaliseFinanceira entity = mapper.toEntity(request);
        repository.save(entity);

        return mapper.toDTO(entity);
    }
}
