package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.LanceOfertado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanceOfertadoRepository extends JpaRepository<LanceOfertado, Long> {
    List<LanceOfertado> findByClienteConsorcioId(Long id);
    List<LanceOfertado> findByClienteConsorcioConsorcioId(Long id);
    List<LanceOfertado> findByClienteConsorcioUsuarioId(Long id);
}
