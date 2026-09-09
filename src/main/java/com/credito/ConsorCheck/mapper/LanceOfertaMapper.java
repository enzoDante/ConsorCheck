package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.LanceOfertaResponseDTO;
import com.credito.ConsorCheck.model.LanceOfertado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LanceOfertaMapper {
    @Mapping(target = "idClienteConsorcio", source = "clienteConsorcio.id")
    LanceOfertaResponseDTO toDTO(LanceOfertado entity);
}
