package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.DadosConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosConsorcioRepository extends JpaRepository<DadosConsorcio, Long> {
}
