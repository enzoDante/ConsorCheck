package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.model.EmpresaCliente;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.EmpresaClienteRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.validation.UserVerify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaClienteService {
    private final EmpresaClienteRepository empresaClienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UserVerify userVerify;
    public EmpresaClienteService(EmpresaClienteRepository empresaClienteRepository, UsuarioRepository repository, UserVerify userVerify){
        this.empresaClienteRepository = empresaClienteRepository;
        this.usuarioRepository = repository;
        this.userVerify = userVerify;
    }

    @Transactional
    public void criarRelacao(Long idEmpresa, Long idCliente){
        userVerify.sameUser(idEmpresa);
        if(userVerify.empresaMatchUser(idCliente))
            throw new BusinessException("Cliente ja tem vínculo com esta empresa");

        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new SQLException("Cliente não encontrado"));
        Usuario empresa = usuarioRepository.findById(idEmpresa)
                .orElseThrow(() -> new SQLException("Empresa não encontrada"));
        EmpresaCliente vinculo = new EmpresaCliente();
        vinculo.setEmpresa(empresa);
        vinculo.setCliente(cliente);
        empresaClienteRepository.save(vinculo);
    }

    @Transactional
    public void removerRelacao(Long id){
        EmpresaCliente vinculo = empresaClienteRepository.findById(id)
                .orElseThrow(() -> new SQLException("Vinculo não encontrado"));

        userVerify.sameUser(vinculo.getEmpresa().getId());
        empresaClienteRepository.delete(vinculo);
    }
}
