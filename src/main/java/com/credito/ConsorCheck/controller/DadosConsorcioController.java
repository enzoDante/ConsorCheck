package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.DadosConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.DadosConsorcioResponseDTO;
import com.credito.ConsorCheck.service.DadosConsorcioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consorcio")
public class DadosConsorcioController {
    private final DadosConsorcioService dadosConsorcioService;
    public DadosConsorcioController(DadosConsorcioService dadosConsorcioService){
        this.dadosConsorcioService = dadosConsorcioService;
    }

    /*@PreAuthorize("hasRole('ADMIN')") ou @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'EMPRESA')")*/
    @PostMapping
    @PreAuthorize("hasROle('EMPRESA')")
    public ResponseEntity<DadosConsorcioResponseDTO> criar(@RequestBody DadosConsorcioRequestDTO request){
        DadosConsorcioResponseDTO response = dadosConsorcioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasROle('EMPRESA')")
    public ResponseEntity<DadosConsorcioResponseDTO> update(@PathVariable Long id, @RequestBody DadosConsorcioRequestDTO requestDTO){
        DadosConsorcioResponseDTO response = dadosConsorcioService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/ativar-desativar")
    @PreAuthorize("hasROle('EMPRESA')")
    public  ResponseEntity<Void> statusHandler(@PathVariable Long id){
        dadosConsorcioService.desativarAtivarConsorcio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPRESA')")
    public ResponseEntity<List<DadosConsorcioResponseDTO>> getAll(){
        List<DadosConsorcioResponseDTO> response = dadosConsorcioService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{idEmpresa}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPRESA')")
    public ResponseEntity<List<DadosConsorcioResponseDTO>> getAllByEmpresa(@PathVariable Long idEmpresa){
        List<DadosConsorcioResponseDTO> response = dadosConsorcioService.getAllByEmpresa(idEmpresa);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPRESA')")
    public ResponseEntity<DadosConsorcioResponseDTO> getConsorcio(@PathVariable Long id){
        DadosConsorcioResponseDTO response = dadosConsorcioService.getConsorcio(id);
        return ResponseEntity.ok(response);
    }
}
