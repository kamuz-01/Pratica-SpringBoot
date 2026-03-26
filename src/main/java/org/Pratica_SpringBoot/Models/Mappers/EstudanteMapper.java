package org.Pratica_SpringBoot.Models.Mappers;

import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstudanteMapper {

    @Mapping(target = "id_usuario", source = "id")
    @Mapping(target = "senha", ignore = true)
    EstudanteDTO toDto(Estudante estudante);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "imagemPerfil", ignore = true)
    Estudante toEntity(EstudanteDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "imagemPerfil", ignore = true)
    void updateEntityFromDto(EstudanteDTO dto, @MappingTarget Estudante estudante);
}
