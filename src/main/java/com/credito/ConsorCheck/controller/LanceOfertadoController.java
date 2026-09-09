package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.LanceOfertaRequestDTO;
import com.credito.ConsorCheck.dto.LanceOfertaResponseDTO;
import com.credito.ConsorCheck.service.LanceOfertadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lance-ofertado")
public class LanceOfertadoController {
    private final LanceOfertadoService lanceOfertadoService;
    public LanceOfertadoController(LanceOfertadoService lanceOfertadoService){
        this.lanceOfertadoService = lanceOfertadoService;
    }

    @PostMapping
    public ResponseEntity<LanceOfertaResponseDTO> criar(LanceOfertaRequestDTO request){
        LanceOfertaResponseDTO response = lanceOfertadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LanceOfertaResponseDTO> get(@PathVariable Long id){
        LanceOfertaResponseDTO response = lanceOfertadoService.get(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/cliente-consorcio/{id}")
    public ResponseEntity<List<LanceOfertaResponseDTO>> getByClienteConsorcioId(@PathVariable Long id){
        List<LanceOfertaResponseDTO> response = lanceOfertadoService.getByClienteConsorcioId(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/consorcio/{id}")
    public ResponseEntity<List<LanceOfertaResponseDTO>> getByConsorcioId(@PathVariable Long id){
        List<LanceOfertaResponseDTO> response = lanceOfertadoService.getByConsorcioId(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<LanceOfertaResponseDTO>> getByClienteId(@PathVariable Long id){
        List<LanceOfertaResponseDTO> response = lanceOfertadoService.getByClienteId(id);
        return ResponseEntity.ok(response);
    }
}
