package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.LanceOfertaRequestDTO;
import com.credito.ConsorCheck.dto.LanceOfertaResponseDTO;
import com.credito.ConsorCheck.enums.Role;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.LanceOfertaMapper;
import com.credito.ConsorCheck.model.ClienteConsorcio;
import com.credito.ConsorCheck.model.LanceOfertado;
import com.credito.ConsorCheck.repository.ClienteConsorcioRepository;
import com.credito.ConsorCheck.repository.LanceOfertadoRepository;
import com.credito.ConsorCheck.security.UserDetailsImpl;
import com.credito.ConsorCheck.validation.UserVerify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LanceOfertadoService {
    private final LanceOfertadoRepository lanceOfertadoRepository;
    private final ClienteConsorcioRepository clienteConsorcioRepository;
    private final LanceOfertaMapper lanceOfertaMapper;
    private final UserVerify userVerify;
    public LanceOfertadoService(LanceOfertadoRepository repository, ClienteConsorcioRepository consorcioRepository, LanceOfertaMapper mapper, UserVerify userVerify){
        this.lanceOfertadoRepository = repository;
        this.clienteConsorcioRepository = consorcioRepository;
        this.lanceOfertaMapper = mapper;
        this.userVerify = userVerify;
    }

    @Transactional
    public LanceOfertaResponseDTO criar(LanceOfertaRequestDTO request){
        UserDetailsImpl userDetails = userVerify.verifyAuth();
        if(!userDetails.getRole().equals(Role.EMPRESA))
            throw new BusinessException("Só pode acessar como usuário Empresa");
        ClienteConsorcio clienteConsorcio = clienteConsorcioRepository.findById(request.getIdClienteConsorcio())
                .orElseThrow(() -> new SQLException("Não foi possível encontrar essa relação de cliente e consorcio"));
        LanceOfertado oferta = new LanceOfertado();
        oferta.setValor(request.getValor());
        oferta.setDataOferta(request.getDataOferta());
        oferta.setContemplado(request.isContemplado());
        oferta.setClienteConsorcio(clienteConsorcio);
        lanceOfertadoRepository.save(oferta);

        return lanceOfertaMapper.toDTO(oferta);
    }

    @Transactional(readOnly = true)
    public LanceOfertaResponseDTO get(Long id){
        LanceOfertado ofertado = lanceOfertadoRepository.findById(id)
                .orElseThrow(() -> new SQLException("Não foi possível encontrar esse Lance Ofertado"));
        return lanceOfertaMapper.toDTO(ofertado);
    }

    @Transactional(readOnly = true)
    public List<LanceOfertaResponseDTO> getByClienteConsorcioId(Long idClienteConsorcio){
        List<LanceOfertado> lances = lanceOfertadoRepository.findByClienteConsorcioId(idClienteConsorcio);
        return lances.stream().map(lanceOfertaMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<LanceOfertaResponseDTO> getByConsorcioId(Long idConsorcio){
        List<LanceOfertado> lances = lanceOfertadoRepository.findByClienteConsorcioConsorcioId(idConsorcio);
        return lances.stream().map(lanceOfertaMapper::toDTO).toList();
    }
    @Transactional(readOnly = true)
    public List<LanceOfertaResponseDTO> getByClienteId(Long idCliente){
        List<LanceOfertado> lances = lanceOfertadoRepository.findByClienteConsorcioUsuarioId(idCliente);
        return lances.stream().map(lanceOfertaMapper::toDTO).toList();
    }
}
