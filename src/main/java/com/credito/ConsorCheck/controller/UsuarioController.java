package com.credito.ConsorCheck.controller;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<UsuarioResponseDTO>> getUsers(){
        List<UsuarioResponseDTO> users = usuarioService.getAll();
        return ResponseEntity.ok(users);
    }
}
