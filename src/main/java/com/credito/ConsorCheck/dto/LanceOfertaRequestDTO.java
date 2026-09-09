package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LanceOfertaRequestDTO {
    private Long idClienteConsorcio;
    private BigDecimal valor;
    private LocalDate dataOferta;
    private boolean contemplado;
}
