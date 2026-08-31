package com.credito.ConsorCheck.dto;

import com.credito.ConsorCheck.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String documento;
    private String nomeDono;
    private LocalDateTime dataCriacao;
    private Role role;
    private boolean ativo;
}
