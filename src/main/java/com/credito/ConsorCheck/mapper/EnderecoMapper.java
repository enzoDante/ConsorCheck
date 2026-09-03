package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.EnderecoRequestDTO;
import com.credito.ConsorCheck.dto.EnderecoResponseDTO;
import com.credito.ConsorCheck.model.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Endereco toEntity(EnderecoRequestDTO request);

    EnderecoResponseDTO toDTO(Endereco entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromDto(EnderecoRequestDTO dto, @MappingTarget Endereco entity);
}
