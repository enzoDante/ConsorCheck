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
@Table(name = "lance_ofertado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanceOfertado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_consorcio", nullable = false)
    private ClienteConsorcio clienteConsorcio;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_oferta", nullable = false)
    private LocalDate dataOferta;

    @Column(nullable = false)
    private boolean contemplado = false;
}
