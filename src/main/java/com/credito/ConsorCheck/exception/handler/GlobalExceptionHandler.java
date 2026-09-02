package com.credito.ConsorCheck.exception.handler;

import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.InvalidDataException;
import com.credito.ConsorCheck.exception.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidData(InvalidDataException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Dados Inválidos", ex.getMessage());
    }
    // outros handlers
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex){
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Violação de Regra de Negócio", ex.getMessage());
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handleSQL(BusinessException ex){
        // Para o cliente, você oculta detalhes sensíveis do banco
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Interno", "Ocorreu uma falha no processamento dos dados.");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String title, String detail) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("title", title);
        body.put("detail", detail);
        return ResponseEntity.status(status).body(body);
    }
}
