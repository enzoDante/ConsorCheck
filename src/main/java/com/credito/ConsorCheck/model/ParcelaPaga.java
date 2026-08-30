package com.credito.ConsorCheck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcela_paga", uniqueConstraints = @UniqueConstraint(
        name = "uk_parcela_cliente_numero",
        columnNames = {"id_cliente_consorcio", "numero_parcela"}
))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaPaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_consorcio", nullable = false)
    private ClienteConsorcio clienteConsorcio;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(name = "valor_pago", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
