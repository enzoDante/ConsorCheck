package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.AnaliseFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliseFinanceiraRepository extends JpaRepository<AnaliseFinanceira, Long> {
    List<AnaliseFinanceira> findByUsuarioIdAndDadosConsorcioIdOrderByDataAnaliseDesc(Long idUsuario, Long idConsorcio);
}
