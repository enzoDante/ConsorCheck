package com.credito.ConsorCheck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dados_consorcio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosConsorcio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "numero_parcelas", nullable = false)
    private int numeroParcelas;

    @Column(name = "taxa_administracao", precision = 5, scale = 2, nullable = false)
    private BigDecimal taxaAdministracao;

    @Column(name = "fundo_reserva", precision = 5, scale = 2, nullable = false)
    private BigDecimal fundoReserva;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_inicio_consorcio", nullable = false)
    private LocalDateTime dataInicioConsorcio;

    @Column(name = "data_fim_consorcio", nullable = false)
    private LocalDateTime dataFimConsorcio;

    @Column(nullable = false)
    private boolean ativo = true;
}
