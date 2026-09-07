package com.credito.ConsorCheck.validation;

import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.repository.EmpresaClienteRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserVerify {
    private final UsuarioRepository usuarioRepository;
    private final EmpresaClienteRepository empresaClienteRepository;

    public boolean empresaMatchUser(Long idUsuario){
        UserDetailsImpl userDetails = verifyAuth();
        return empresaClienteRepository.findByEmpresaIdAndClienteId(userDetails.getId(), idUsuario).isPresent();
    }

    public boolean sameUser(Long idUsuario){
        UserDetailsImpl userDetails = verifyAuth();
        if(!userDetails.getId().equals(idUsuario))
            throw new BusinessException("ID incorreto, deve ser o mesmo do usuário autenticado");

        return true;
    }

    private UserDetailsImpl verifyAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        if(!autenticado)
            throw new BusinessException("É necessário estar autenticado para fazer esta ação");
        return (UserDetailsImpl) auth.getPrincipal();
    }
    /*
    *
    * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        if(!autenticado)
            throw new BusinessException("É necessário estar autenticado para cadastrar um cliente vinculado a uma empresa");

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        if(!userDetails.getId().equals(idUsuario) && userDetails.getRole() != Role.EMPRESA)
            throw new BusinessException("ID incorreto de acordo com usuário autenticado");

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new SQLException("Usuário não encontrado"));*/
}
