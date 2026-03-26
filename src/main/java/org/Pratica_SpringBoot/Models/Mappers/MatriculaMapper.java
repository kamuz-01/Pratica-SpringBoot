package org.Pratica_SpringBoot.Models.Mappers;

import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Models.Entities.Matricula;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatriculaMapper {

    @Mapping(target = "id_matricula", source = "id")
    @Mapping(target = "idEstudante", source = "estudante.id")
    @Mapping(target = "idDisciplina", source = "disciplina.id")
    MatriculaDTO toDto(Matricula matricula);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estudante", ignore = true)
    @Mapping(target = "disciplina", ignore = true)
    Matricula toEntity(MatriculaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estudante", ignore = true)
    @Mapping(target = "disciplina", ignore = true)
    @Mapping(target = "dataMatricula", ignore = true)
    void updateEntityFromDto(MatriculaDTO dto, @MappingTarget Matricula matricula);
}
