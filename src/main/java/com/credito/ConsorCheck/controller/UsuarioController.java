package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuário", description = "Endpoints para manter usuários")
public class UsuarioController {
    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody UsuarioRequestDTO request){
        UsuarioResponseDTO response = usuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUser(@PathVariable Long id){
        UsuarioResponseDTO response = usuarioService.getById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    /*@PreAuthorize("hasRole('ADMIN')") ou @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'EMPRESA')")*/
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<UsuarioResponseDTO> users = usuarioService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@RequestBody UsuarioRequestDTO dto, @PathVariable Long id){
        UsuarioResponseDTO user = usuarioService.update(id, dto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/inactive")
    public ResponseEntity<Void> inactiveUser(@PathVariable Long id){
        usuarioService.inactiveUser(id);
        return ResponseEntity.noContent().build();
    }
}
