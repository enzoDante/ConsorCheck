package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.LanceOfertado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanceOfertadoRepository extends JpaRepository<LanceOfertado, Long> {
}
