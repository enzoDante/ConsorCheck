package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.ClienteConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteConsorcioRepository extends JpaRepository<ClienteConsorcio, Long> {
    boolean existsByUsuarioIdAndConsorcioId(Long idUsuario, Long idConsorcio);
    List<ClienteConsorcio> findByUsuarioId(Long idUsuario);
    List<ClienteConsorcio> findByConsorcioId(Long idConsorcio);
}
