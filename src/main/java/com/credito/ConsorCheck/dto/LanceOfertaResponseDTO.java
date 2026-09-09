package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class LanceOfertaResponseDTO {
    private Long id;
    private Long idClienteConsorcio;
    private BigDecimal valor;
    private LocalDateTime dataCriacao;
    private LocalDate dataOferta;
    private boolean contemplado;
}
