package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.AnaliseFinanceiraResponseDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioResponseDTO;
import com.credito.ConsorCheck.enums.StatusCliente;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.AnaliseFinanceiraMapper;
import com.credito.ConsorCheck.mapper.ClienteConsorcioMapper;
import com.credito.ConsorCheck.model.AnaliseFinanceira;
import com.credito.ConsorCheck.model.ClienteConsorcio;
import com.credito.ConsorCheck.model.DadosConsorcio;
import com.credito.ConsorCheck.model.Usuario;
import com.credito.ConsorCheck.repository.AnaliseFinanceiraRepository;
import com.credito.ConsorCheck.repository.ClienteConsorcioRepository;
import com.credito.ConsorCheck.repository.DadosConsorcioRepository;
import com.credito.ConsorCheck.repository.UsuarioRepository;
import com.credito.ConsorCheck.validation.UserVerify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ClienteConsorcioService {
    private final ClienteConsorcioRepository clienteConsorcioRepository;
    private final AnaliseFinanceiraRepository analiseFinanceiraRepository;
    private final UsuarioRepository usuarioRepository;
    private final DadosConsorcioRepository consorcioRepository;
    private final ClienteConsorcioMapper clienteConsorcioMapper;
    private final AnaliseFinanceiraMapper analiseMapper;
    private final UserVerify userVerify;

    public ClienteConsorcioService(ClienteConsorcioRepository repository, AnaliseFinanceiraRepository analiseRepository, UsuarioRepository usuarioRepository, DadosConsorcioRepository consorcioRepository, ClienteConsorcioMapper mapper, AnaliseFinanceiraMapper analiseMapper, UserVerify verify){
        this.clienteConsorcioRepository = repository;
        this.analiseFinanceiraRepository = analiseRepository;
        this.usuarioRepository = usuarioRepository;
        this.consorcioRepository = consorcioRepository;
        this.clienteConsorcioMapper = mapper;
        this.analiseMapper = analiseMapper;
        this.userVerify = verify;
    }

    @Transactional
    public AnaliseFinanceiraResponseDTO criar(ClienteConsorcioRequestDTO request, Long idUsuario){
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

        AnaliseFinanceira analise = new AnaliseFinanceira();
        analise.setUsuario(user);
        analise.setDadosConsorcio(consorcio);
        BigDecimal salario = user.getDadosFinanceiros().getSalario();
        analise.setRendaMensal(salario);
        analise.setParcelasAtuais(comprometimentoMensalAtual); // soma das parcelas atuais com taxa
        BigDecimal comprometimentoPercentual = (comprometimentoMensalAtual.divide(salario)).multiply(BigDecimal.valueOf(100));
        analise.setComprometimentoAtual(comprometimentoPercentual); // percentual do comprometimento do salário do cliente
        analise.setNovaParcela(parcelaNovoConsorcio); // valor da nova parcela + taxa
        BigDecimal comprometimentoPercentualNovo = (comprometimentoTotalProjetado.divide(salario)).multiply(BigDecimal.valueOf(100));
        analise.setComprometimentoProjetado(comprometimentoPercentualNovo); // percentual do comprometimento do salário considerando o novo consórcio

        analise.setLanceEsperado(request.getLanceEsperado());

        boolean possuiCapacidade = comprometimentoTotalProjetado.compareTo(limitePermitido) <= 0;

        analise.setPossuiCapacidadeParcela(possuiCapacidade);
        analise.setPossuiCapacidadeLance(possuiCapacidade);
        analise.setResultado(possuiCapacidade ? "APROVADO" : "REPROVADO");
        analiseFinanceiraRepository.save(analise);
        AnaliseFinanceiraResponseDTO response = analiseMapper.toDTO(analise);
        if(!possuiCapacidade){
            response.setClienteConsorcio(null);
            return response;
        }

        ClienteConsorcio clienteConsorcio = clienteConsorcioMapper.toEntity(request);
        clienteConsorcio.setUsuario(user);
        clienteConsorcio.setConsorcio(consorcio);
        clienteConsorcio.setStatus(StatusCliente.ATIVO);
        clienteConsorcioRepository.save(clienteConsorcio);

        response.setClienteConsorcio(clienteConsorcioMapper.toDTO(clienteConsorcio));

        return response;
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

    @Transactional(readOnly = true)
    public List<AnaliseFinanceiraResponseDTO> listarAnalisesByClienteConsorcio(Long idClienteConsorcio){
        ClienteConsorcio cConsorcio = clienteConsorcioRepository.findById(idClienteConsorcio)
                .orElseThrow(() -> new SQLException("Não foi possível encontrar essa relação"));
        List<AnaliseFinanceira> analises = analiseFinanceiraRepository.findByUsuarioIdAndDadosConsorcioIdOrderByDataAnaliseDesc(cConsorcio.getUsuario().getId(), cConsorcio.getConsorcio().getId());

        return analises.stream().map(analiseMapper::toDTO).toList();
    }
}
