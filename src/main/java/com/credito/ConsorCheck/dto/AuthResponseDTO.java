package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthResponseDTO {
    private String jwtToken;
    private LocalDateTime expiresAt;

}
