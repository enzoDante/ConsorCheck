package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.DadosConsorcioRequestDTO;
import com.credito.ConsorCheck.dto.DadosConsorcioResponseDTO;
import com.credito.ConsorCheck.model.DadosConsorcio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DadosConsorcioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    DadosConsorcio toEntity(DadosConsorcioRequestDTO dto);

    @Mapping(target = "idUsuario", source = "usuario.id")
    DadosConsorcioResponseDTO toDTO(DadosConsorcio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromDto(DadosConsorcioRequestDTO dto, @MappingTarget DadosConsorcio entity);
}
