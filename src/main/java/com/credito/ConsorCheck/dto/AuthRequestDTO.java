package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    private String username; // nome ou email
    private String senha;
}
