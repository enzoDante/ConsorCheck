package com.credito.ConsorCheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private UsuarioResponseDTO userInfo;
    private String token;
    private String refreshToken;
    private String tipo = "Bearer";
}
