package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ParcelaPagaRequestDTO {
    private Long idClienteConsorcio;
    private int numeroParcela;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
}
