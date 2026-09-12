package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.AnaliseFinanceiraResponseDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioResponseDTO;
import com.credito.ConsorCheck.enums.StatusCliente;
import com.credito.ConsorCheck.service.ClienteConsorcioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente-consorcio")
public class ClienteConsorcioController {
    private final ClienteConsorcioService clienteConsorcioService;
    public ClienteConsorcioController(ClienteConsorcioService clienteConsorcioService){
        this.clienteConsorcioService = clienteConsorcioService;
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<AnaliseFinanceiraResponseDTO> criar(@RequestBody ClienteConsorcioRequestDTO request, @PathVariable Long idUsuario){
        AnaliseFinanceiraResponseDTO response = clienteConsorcioService.criar(request, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{idRelacao}")
    public ResponseEntity<Void> mudarStatusCliente(@PathVariable Long idRelacao, @RequestBody StatusCliente status){
        clienteConsorcioService.mudarStatusCliente(idRelacao, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idUsuario}")
    public ResponseEntity<List<ClienteConsorcioResponseDTO>> getByUsuario(@PathVariable Long idUsuario){
        List<ClienteConsorcioResponseDTO> response = clienteConsorcioService.getByUsuario(idUsuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<List<ClienteConsorcioResponseDTO>> getByConsorcio(@PathVariable Long idConsorcio){
        List<ClienteConsorcioResponseDTO> response = clienteConsorcioService.getByConsorcio(idConsorcio);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analise/{idClienteConsorcio}")
    public ResponseEntity<List<AnaliseFinanceiraResponseDTO>> listarAnalisesByClienteConsorcioId(@PathVariable Long idClienteConsorcio){
        List<AnaliseFinanceiraResponseDTO> analises = clienteConsorcioService.listarAnalisesByClienteConsorcio(idClienteConsorcio);
        return ResponseEntity.ok(analises);
    }
}
