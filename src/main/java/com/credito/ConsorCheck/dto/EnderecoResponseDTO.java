package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EnderecoResponseDTO {
    private Long idUsuario;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
