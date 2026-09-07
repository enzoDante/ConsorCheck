package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.DadosFinanceirosRequestDTO;
import com.credito.ConsorCheck.service.DadosFinanceirosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dados_financeiros")
public class DadosFinanceirosController {
    private final DadosFinanceirosService dadosFinanceirosService;
    public DadosFinanceirosController(DadosFinanceirosService dadosFinanceirosService){
        this.dadosFinanceirosService = dadosFinanceirosService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(DadosFinanceirosRequestDTO request){
        dadosFinanceirosService.criar(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(DadosFinanceirosRequestDTO request){
        dadosFinanceirosService.update(request);
        return ResponseEntity.noContent().build();
    }
}
