package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.DadosFinaneciros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosFinanceirosRepository extends JpaRepository<DadosFinaneciros, Long> {
}
