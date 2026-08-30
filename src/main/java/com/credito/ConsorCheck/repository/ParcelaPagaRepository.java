package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.ParcelaPaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaPagaRepository extends JpaRepository<ParcelaPaga, Long> {
}
