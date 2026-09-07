package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.DadosFinanceiros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DadosFinanceirosRepository extends JpaRepository<DadosFinanceiros, Long> {
}
