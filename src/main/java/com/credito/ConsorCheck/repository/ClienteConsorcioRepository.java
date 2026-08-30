package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.ClienteConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteConsorcioRepository extends JpaRepository<ClienteConsorcio, Long> {
}
