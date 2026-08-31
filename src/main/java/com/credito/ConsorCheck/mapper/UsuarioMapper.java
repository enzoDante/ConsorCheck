package com.credito.ConsorCheck.mapper;

import com.credito.ConsorCheck.dto.UsuarioRequestDTO;
import com.credito.ConsorCheck.dto.UsuarioResponseDTO;
import com.credito.ConsorCheck.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "dadosFinanceiros", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toDTO(Usuario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "dadosFinanceiros", ignore = true)
    void updateEntityFromDto(UsuarioRequestDTO dto, @MappingTarget Usuario entity);
}
