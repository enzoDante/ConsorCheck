package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.ParcelaPagaResponseDTO;
import com.credito.ConsorCheck.model.ParcelaPaga;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelaPagaMapper {
    @Mapping(target = "idClienteConsorcio", source = "clienteConsorcio.id")
    ParcelaPagaResponseDTO toDTO(ParcelaPaga entity);
}
