package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.AnaliseFinanceiraResponseDTO;
import com.credito.ConsorCheck.model.AnaliseFinanceira;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnaliseFinanceiraMapper {

    @Mapping(target = "idUsuario", source = "usuario.id")
    @Mapping(target = "idConsorcio", source = "dadosConsorcio.id")
    AnaliseFinanceiraResponseDTO toDTO(AnaliseFinanceira entity);
}
