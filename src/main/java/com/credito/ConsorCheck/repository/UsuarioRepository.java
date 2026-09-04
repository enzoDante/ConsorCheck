package com.credito.ConsorCheck.repository;

import com.credito.ConsorCheck.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(
        value = "SELECT * FROM usuario u WHERE MATCH(u.nome, u.email, u.nome_dono) AGAINST(:valor IN BOOLEAN MODE)", // boolean ou natural mode
        nativeQuery = true
    )
    List<Usuario> buscarPorTexto(@Param("valor") String valor);

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDocumento(String doc);
    boolean existsByDocumento(String doc);
    Optional<Usuario> findByIdAndAtivo(Long id, boolean ativo);

    Optional<Usuario> findByNomeOrEmail(String nome, String email);

    @Query("SELECT u FROM Usuario u WHERE (u.nome = :username OR u.email = :username) AND u.senha = :senha")
    Optional<Usuario> findUserLogin(@Param("username") String username, @Param("senha") String senha);
}
