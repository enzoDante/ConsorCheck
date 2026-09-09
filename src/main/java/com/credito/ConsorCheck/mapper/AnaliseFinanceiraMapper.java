package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.AnaliseFinanceiraRequestDTO;
import com.credito.ConsorCheck.dto.AnaliseFinanceiraResponseDTO;
import com.credito.ConsorCheck.model.AnaliseFinanceira;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnaliseFinanceiraMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAnalise", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "consorcio", ignore = true)
    AnaliseFinanceira toEntity(AnaliseFinanceiraRequestDTO request);

    @Mapping(target = "idUsuario", source = "usuario.id")
    @Mapping(target = "idConsorcio", source = "consorcio.id")
    AnaliseFinanceiraResponseDTO toDTO(AnaliseFinanceira entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAnalise", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "consorcio", ignore = true)
    void updateEntityFromDto(AnaliseFinanceiraRequestDTO dto, @MappingTarget AnaliseFinanceira entity);
}
