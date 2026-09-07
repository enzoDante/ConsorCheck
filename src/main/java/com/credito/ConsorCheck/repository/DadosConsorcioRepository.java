package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.DadosConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosConsorcioRepository extends JpaRepository<DadosConsorcio, Long> {
    List<DadosConsorcio> findByUsuarioId(Long idUsuario);
}
