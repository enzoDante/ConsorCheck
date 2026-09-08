package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ParcelaPagaResponseDTO {
    private Long id;
    private Long idClienteConsorcio;
    private int numeroParcela;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private LocalDateTime dataCriacao;
}
