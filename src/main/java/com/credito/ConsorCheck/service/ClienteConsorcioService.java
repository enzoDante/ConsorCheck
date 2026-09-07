package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.ClienteConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioResponseDTO;
import com.credito.ConsorCheck.enums.StatusCliente;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.ClienteConsorcioMapper;
import com.credito.ConsorCheck.model.ClienteConsorcio;
import com.credito.ConsorCheck.model.DadosConsorcio;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.ClienteConsorcioRepository;
import com.credito.ConsorCheck.repository.DadosConsorcioRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import com.credito.ConsorCheck.validation.UserVerify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ClienteConsorcioService {
    private final ClienteConsorcioRepository clienteConsorcioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DadosConsorcioRepository consorcioRepository;
    private final ClienteConsorcioMapper clienteConsorcioMapper;
    private final UserVerify userVerify;

    public ClienteConsorcioService(ClienteConsorcioRepository repository, UsuarioRepository usuarioRepository, DadosConsorcioRepository consorcioRepository, ClienteConsorcioMapper mapper, UserVerify verify){
        this.clienteConsorcioRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.consorcioRepository = consorcioRepository;
        this.clienteConsorcioMapper = mapper;
        this.userVerify = verify;
    }

    @Transactional
    public ClienteConsorcioResponseDTO criar(ClienteConsorcioRequestDTO request, Long idUsuario){
        if(!userVerify.sameUser(idUsuario) && !userVerify.empresaMatchUser(idUsuario))
            throw new BusinessException("Empresa não tem vínculo com este usuário");
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new SQLException("Usuário não encontrado"));
        DadosConsorcio consorcio = consorcioRepository.findById(request.getIdConsorcio())
                .orElseThrow(() -> new SQLException("Consórcio não encontrado"));
        if(clienteConsorcioRepository.existsByUsuarioIdAndConsorcioId(idUsuario, request.getIdConsorcio()))
            throw new BusinessException("Usuário ja está vinculado a este consórcio");

        List<ClienteConsorcio> consorciosCliente = clienteConsorcioRepository.findByUsuarioId(idUsuario);
        BigDecimal comprometimentoMensalAtual = BigDecimal.ZERO;
        for(ClienteConsorcio cons : consorciosCliente){
            DadosConsorcio c = cons.getConsorcio();
            if(cons.getStatus().equals(StatusCliente.ATIVO) && c.isAtivo()){
                BigDecimal parcelaMensal = c.getValor()
                        .multiply(BigDecimal.ONE.add(c.getTaxaAdministracao().divide(BigDecimal.valueOf(100))))
                        .divide(BigDecimal.valueOf(c.getNumeroParcelas()), 2, RoundingMode.HALF_UP);

                comprometimentoMensalAtual = comprometimentoMensalAtual.add(parcelaMensal);
            }
        }
        BigDecimal parcelaNovoConsorcio = consorcio.getValor()
                .multiply(BigDecimal.ONE.add(consorcio.getTaxaAdministracao().divide(BigDecimal.valueOf(100))))
                .divide(BigDecimal.valueOf(consorcio.getNumeroParcelas()), 2, RoundingMode.HALF_UP);

        BigDecimal comprometimentoTotalProjetado = comprometimentoMensalAtual.add(parcelaNovoConsorcio);
        BigDecimal limitePermitido = user.getDadosFinanceiros().getSalario()
                .multiply(consorcio.getComprometimentoMaximo().divide(BigDecimal.valueOf(100)));

        if (comprometimentoTotalProjetado.compareTo(limitePermitido) > 0) {
            throw new BusinessException("Comprometimento de renda excede o limite permitido para este consórcio");
        }

        ClienteConsorcio clienteConsorcio = clienteConsorcioMapper.toEntity(request);
        clienteConsorcio.setUsuario(user);
        clienteConsorcio.setConsorcio(consorcio);
        clienteConsorcio.setStatus(StatusCliente.ATIVO);
        clienteConsorcioRepository.save(clienteConsorcio);

        return clienteConsorcioMapper.toDTO(clienteConsorcio);
    }

    @Transactional
    public void mudarStatusCliente(Long id, StatusCliente status){
        ClienteConsorcio consorcio = clienteConsorcioRepository.findById(id)
                .orElseThrow(() -> new SQLException("Relação não encontrada"));
        consorcio.setStatus(status);
    }

    @Transactional(readOnly = true)
    public List<ClienteConsorcioResponseDTO> getByUsuario(Long idUsuario){
        List<ClienteConsorcio> consorcios = clienteConsorcioRepository.findByUsuarioId(idUsuario);
        return consorcios.stream().map(clienteConsorcioMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteConsorcioResponseDTO> getByConsorcio(Long idConsorcio){
        List<ClienteConsorcio> consorcios = clienteConsorcioRepository.findByConsorcioId(idConsorcio);
        return consorcios.stream().map(clienteConsorcioMapper::toDTO).toList();
    }
}
