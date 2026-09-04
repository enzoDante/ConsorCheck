package com.credito.ConsorCheck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "analise_financeira")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnaliseFinanceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio", nullable = false)
    private DadosConsorcio dadosConsorcio;

    @Column(name = "renda_mensal", precision = 15, scale = 2, nullable = false)
    private BigDecimal rendaMensal;

    @Column(name = "parcelas_atuais", precision = 15, scale = 2, nullable = false)
    private BigDecimal parcelasAtuais;

    @Column(name = "nova_parcela", precision = 15, scale = 2, nullable = false)
    private BigDecimal novaParcela;

    @Column(name = "comprometimento_atual", precision = 5, scale = 2, nullable = false)
    private BigDecimal comprometimentoAtual;

    @Column(name = "comprometimento_projetado", precision = 5, scale = 2, nullable = false)
    private BigDecimal comprometimentoProjetado;

    @Column(name = "lance_esperado", precision = 15, scale = 2)
    private BigDecimal lanceEsperado;

    @Column(name = "possui_capacidade_parcela", nullable = false)
    private boolean possuiCapacidadeParcela;

    @Column(name = "possui_capacidade_lance", nullable = false)
    private boolean possuiCapacidadeLance;

    @Column(length = 20, nullable = false)
    private String resultado;

    @Column(name = "data_analise", nullable = false, updatable = false)
    private LocalDateTime dataAnalise = LocalDateTime.now();
}
