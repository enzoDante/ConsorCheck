package com.credito.ConsorCheck.dto;

import com.credito.ConsorCheck.enums.StatusCliente;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteConsorcioResponseDTO {
    private Long id;
    private Long idUsuario;
    private Long idConsorcio;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private BigDecimal lanceEsperado;
    private boolean contemplado;
    private StatusCliente status;
}
