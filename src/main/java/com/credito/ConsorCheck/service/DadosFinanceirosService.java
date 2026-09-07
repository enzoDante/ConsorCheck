package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.DadosFinanceirosRequestDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.model.DadosFinanceiros;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.DadosFinanceirosRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DadosFinanceirosService {
    private final DadosFinanceirosRepository dadosFinanceirosRepository;
    private final UsuarioRepository usuarioRepository;
    public DadosFinanceirosService(DadosFinanceirosRepository dadosFinanceirosRepository, UsuarioRepository usuarioRepository){
        this.dadosFinanceirosRepository = dadosFinanceirosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void criar(DadosFinanceirosRequestDTO request){
        Usuario user = validarUsuario(request.getIdUsuario());

        DadosFinanceiros dadosFinanceiros = new DadosFinanceiros();
        dadosFinanceiros.setUsuario(user);
        dadosFinanceiros.setSalario(request.getSalario());
        dadosFinanceirosRepository.save(dadosFinanceiros);
    }

    @Transactional
    public void update(DadosFinanceirosRequestDTO request){
        validarUsuario(request.getIdUsuario());

        DadosFinanceiros dadosFinanceiros = dadosFinanceirosRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new SQLException("Dados Financeiros não encontrado!"));
        dadosFinanceiros.setSalario(request.getSalario());
        dadosFinanceirosRepository.save(dadosFinanceiros);
    }
    private Usuario validarUsuario(Long idUsuario){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        if(!autenticado)
            throw new BusinessException("É necessário estar autenticado para cadastrar um cliente vinculado a uma empresa");

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        if(!userDetails.getId().equals(idUsuario) && userDetails.getRole() != Role.EMPRESA)
            throw new BusinessException("ID incorreto de acordo com usuário autenticado");

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new SQLException("Usuário não encontrado"));
    }
}
