package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnaliseFinanceiraResponseDTO {
    private Long id;
    private Long idUsuario;
    private Long idConsorcio;
    private BigDecimal rendaMensal;
    private BigDecimal parcelasAtuais;
    private BigDecimal novaParcela;
    private BigDecimal comprometimentoAtual;
    private BigDecimal comprometimentoProjetado;
    private BigDecimal lanceEsperado;
    private boolean possuiCapacidadeParcela;
    private boolean possuiCapacidadeLance;
    private String resultado;
    private LocalDateTime dataAnalise;
}
