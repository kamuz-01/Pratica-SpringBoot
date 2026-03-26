package org.Pratica_SpringBoot.Models.Mappers;

import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessorMapper {

    @Mapping(target = "id_usuario", source = "id")
    ProfessorDTO toDto(Professor professor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "imagemPerfil", ignore = true)
    Professor toEntity(ProfessorDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "imagemPerfil", ignore = true)
    void updateEntityFromDto(ProfessorDTO dto, @MappingTarget Professor professor);
}
