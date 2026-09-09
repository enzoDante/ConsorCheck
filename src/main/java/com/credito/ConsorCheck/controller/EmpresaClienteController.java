package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.service.EmpresaClienteService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa-cliente")
public class EmpresaClienteController {
    private final EmpresaClienteService empresaClienteService;
    public EmpresaClienteController(EmpresaClienteService service){
        this.empresaClienteService = service;
    }

    @PostMapping("/empresa/{idEmpresa}/cliente/{idCliente}")
    public ResponseEntity<Void> criarVinculo(@PathVariable Long idEmpresa, @PathVariable Long idCliente){
        empresaClienteService.criarRelacao(idEmpresa, idCliente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVinculo(@PathVariable Long id){
        empresaClienteService.removerRelacao(id);
        return ResponseEntity.noContent().build();
    }
}
