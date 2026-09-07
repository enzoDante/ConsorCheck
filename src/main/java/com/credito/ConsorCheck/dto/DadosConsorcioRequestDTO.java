package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DadosConsorcioRequestDTO {
    private Long idUsuario;
    private String nome;
    private BigDecimal valor; // valor total da carta de crédito
    private int numeroParcelas; // prazo total do plano (ex: 180 meses)
    private BigDecimal taxaAdministracao; // percentual sobre o valor da carta (ex: 18.50)
    private BigDecimal fundoReserva; //percentual, opcional em muitos planos
    private BigDecimal comprometimentoMaximo; //percentual do quanto o cliente deve dedicar seu salário Ex.: "não permitir que as parcelas ultrapassem 40% da renda".
    private LocalDateTime dataInicioConsorcio;
    private LocalDateTime dataFimConsorcio;
    private boolean ativo = true;
}
