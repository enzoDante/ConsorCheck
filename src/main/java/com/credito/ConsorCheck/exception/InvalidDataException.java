package com.credito.ConsorCheck.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidDataException extends RuntimeException {
    private final List<String> erros;
    public InvalidDataException(String message, List<String> erros){
        super(message);
        this.erros = erros;
        // no service deve fazer:
        // throw new InvalidDataException("Campos incorretos", List.of("email inválido", "senha fraca"));
        // no handler faça: ex.getErros()
    }

}
