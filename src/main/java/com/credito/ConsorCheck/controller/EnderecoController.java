package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.EnderecoRequestDTO;
import com.credito.ConsorCheck.dto.EnderecoResponseDTO;
import com.credito.ConsorCheck.service.EnderecoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Endereco")
@Tag(name = "Endereço", description = "Endpoints para cadastro e atualização de endereço")
public class EnderecoController {
    private final EnderecoService enderecoService;
    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> criar(@RequestBody EnderecoRequestDTO dto){
        EnderecoResponseDTO response = enderecoService.criar(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> update(@RequestBody EnderecoRequestDTO dto, @PathVariable Long id){
        EnderecoResponseDTO response = enderecoService.update(dto, id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> getEndereco(@PathVariable Long id){
        EnderecoResponseDTO response = enderecoService.get(id);
        return ResponseEntity.ok(response);
    }
}
