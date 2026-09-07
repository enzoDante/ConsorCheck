package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.ClienteConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.ClienteConsorcioResponseDTO;
import com.credito.ConsorCheck.model.ClienteConsorcio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteConsorcioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "consorcio", ignore = true)
    ClienteConsorcio toEntity(ClienteConsorcioRequestDTO dto);

    @Mapping(target = "idUsuario", source = "usuario.id")
    @Mapping(target = "idConsorcio", source = "consorcio.id")
    ClienteConsorcioResponseDTO toDTO(ClienteConsorcio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "consorcio", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromDto(ClienteConsorcioRequestDTO dto, @MappingTarget ClienteConsorcio entity);
}
