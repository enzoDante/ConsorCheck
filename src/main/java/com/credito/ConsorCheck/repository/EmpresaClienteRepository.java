package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.EmpresaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaClienteRepository extends JpaRepository<EmpresaCliente, Long> {
    Optional<EmpresaCliente> findByEmpresaIdAndClienteId(Long idEmpresa, Long idCliente);
}
