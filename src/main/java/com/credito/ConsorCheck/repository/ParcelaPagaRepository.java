package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.ParcelaPaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelaPagaRepository extends JpaRepository<ParcelaPaga, Long> {
    boolean existsByClienteConsorcioIdAndNumeroParcela(Long idConsorcio, int numeroParcela);
    long countByClienteConsorcioId(Long clienteConsorcioId);

    List<ParcelaPaga> findByClienteConsorcioId(Long idClienteConsorcio);
    List<ParcelaPaga> findByClienteConsorcioUsuarioId(Long idUsuario);
    List<ParcelaPaga> findByClienteConsorcioConsorcioId(Long idConsorcio);
}
