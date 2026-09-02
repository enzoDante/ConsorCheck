package com.credito.ConsorCheck.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    private String user; // nome ou email
    private String senha;
}
