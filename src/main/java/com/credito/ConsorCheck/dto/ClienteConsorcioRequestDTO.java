package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ClienteConsorcioRequestDTO {
    private Long idConsorcio;
    private BigDecimal lanceEsperado;
}
