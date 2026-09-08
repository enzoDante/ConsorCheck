package com.credito.ConsorCheck.service;

import com.credito.ConsorCheck.dto.ParcelaPagaRequestDTO;
import com.credito.ConsorCheck.dto.ParcelaPagaResponseDTO;
import com.credito.ConsorCheck.enums.StatusCliente;
import com.credito.ConsorCheck.exception.BusinessException;
import com.credito.ConsorCheck.exception.SQLException;
import com.credito.ConsorCheck.mapper.ParcelaPagaMapper;
import com.credito.ConsorCheck.model.ClienteConsorcio;
import com.credito.ConsorCheck.model.ParcelaPaga;
import com.credito.ConsorCheck.repository.ClienteConsorcioRepository;
import com.credito.ConsorCheck.repository.ParcelaPagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ParcelaPagaService {
    private final ParcelaPagaRepository parcelaPagaRepository;
    private final ClienteConsorcioRepository clienteConsorcioRepository;
    private final ParcelaPagaMapper parcelaPagaMapper;
    public ParcelaPagaService(ParcelaPagaMapper mapper, ParcelaPagaRepository repository, ClienteConsorcioRepository clienteConsorcioRepository){
        this.parcelaPagaMapper = mapper;
        this.parcelaPagaRepository = repository;
        this.clienteConsorcioRepository = clienteConsorcioRepository;
    }

    @Transactional
    public ParcelaPagaResponseDTO criar(ParcelaPagaRequestDTO request){
        ClienteConsorcio clienteConsorcio = clienteConsorcioRepository.findById(request.getIdClienteConsorcio())
                .orElseThrow(() -> new SQLException("Não foi possível encontrar essa relação"));
        int totalParcelas = clienteConsorcio.getConsorcio().getNumeroParcelas();
        if(request.getNumeroParcela() > totalParcelas)
            throw new BusinessException("Número de parcela excede o total do plano (" + totalParcelas + ")");
        if(request.getDataPagamento().isAfter(LocalDate.now()))
            throw new BusinessException("Data de pagamento não pode ser no futuro");
        if(parcelaPagaRepository.existsByClienteConsorcioIdAndNumeroParcela(request.getIdClienteConsorcio(), request.getNumeroParcela()))
            throw new BusinessException("Essa parcela já foi registrada");

        ParcelaPaga parcela = new ParcelaPaga();
        parcela.setClienteConsorcio(clienteConsorcio);
        parcela.setValorPago(request.getValorPago());
        parcela.setDataPagamento(request.getDataPagamento());
        parcela.setNumeroParcela(request.getNumeroParcela());
        parcelaPagaRepository.save(parcela);
        // Verifica se essa foi a última parcela do plano — se sim, marca como finalizado
        long totalPago = parcelaPagaRepository.countByClienteConsorcioId(request.getIdClienteConsorcio());
        if (totalPago == totalParcelas) {
            clienteConsorcio.setStatus(StatusCliente.APROVADO);
            clienteConsorcioRepository.save(clienteConsorcio);
        }

        return parcelaPagaMapper.toDTO(parcela);
    }
}
