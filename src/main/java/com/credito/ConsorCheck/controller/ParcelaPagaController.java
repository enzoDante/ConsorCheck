package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.ParcelaPagaRequestDTO;
import com.credito.ConsorCheck.dto.ParcelaPagaResponseDTO;
import com.credito.ConsorCheck.enums.StatusCliente;
import com.credito.ConsorCheck.service.ParcelaPagaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcela-paga")
public class ParcelaPagaController {
    private final ParcelaPagaService parcelaPagaService;
    public ParcelaPagaController(ParcelaPagaService parcelaPagaService){
        this.parcelaPagaService = parcelaPagaService;
    }

    @PostMapping
    public ResponseEntity<ParcelaPagaResponseDTO> criar(@RequestBody ParcelaPagaRequestDTO request){
        ParcelaPagaResponseDTO response = parcelaPagaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestBody StatusCliente status){
        parcelaPagaService.alterarStatus(id, status);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ParcelaPagaResponseDTO> getParcela(@PathVariable Long id){
        ParcelaPagaResponseDTO parcela = parcelaPagaService.getParcela(id);
        return ResponseEntity.ok(parcela);
    }
    @GetMapping("/cliente-consorcio/{id}")
    public ResponseEntity<List<ParcelaPagaResponseDTO>> getParcelasByClienteConsorcioId(@PathVariable Long id){
        List<ParcelaPagaResponseDTO> parcelas = parcelaPagaService.getParcelasByIdClienteConsorcio(id);
        return ResponseEntity.ok(parcelas);
    }
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<ParcelaPagaResponseDTO>> getParcelasByClienteId(@PathVariable Long id){
        List<ParcelaPagaResponseDTO> parcelas = parcelaPagaService.getParcelasByIdCliente(id);
        return ResponseEntity.ok(parcelas);
    }
    @GetMapping("/consorcio/{id}")
    public ResponseEntity<List<ParcelaPagaResponseDTO>> getParcelasByConsorcioId(@PathVariable Long id){
        List<ParcelaPagaResponseDTO> parcelas = parcelaPagaService.getParcelasByIdConsorcio(id);
        return ResponseEntity.ok(parcelas);
    }
}
