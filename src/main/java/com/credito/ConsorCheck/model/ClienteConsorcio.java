package com.credito.ConsorCheck.model;

import com.credito.ConsorCheck.enums.StatusCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_consorcio", uniqueConstraints = @UniqueConstraint(
        name = "uk_cliente_consorcio",
        columnNames = {"id_usuario", "id_consorcio"}
))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteConsorcio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio", nullable = false)
    private DadosConsorcio consorcio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCliente status = StatusCliente.PENDENTE;

    @Column(name = "lance_esperado", precision = 15, scale = 2)
    private BigDecimal lanceEsperado;

    @Column(nullable = false)
    private boolean contemplado =false;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
