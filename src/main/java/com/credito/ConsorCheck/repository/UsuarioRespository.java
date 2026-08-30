package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRespository extends JpaRepository<Usuario, Long> {
}
