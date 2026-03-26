package org.Pratica_SpringBoot.Models.Mappers;

import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DisciplinaMapper {

    @Mapping(target = "nome", source = "nomeDisciplina")
    @Mapping(target = "descricao", source = "descricaoDisciplina")
    @Mapping(target = "codigo", source = "codigoDisciplina")
    @Mapping(target = "id_disciplina", source = "id")
    @Mapping(target = "id_curso", source = "curso.id")
    @Mapping(target = "id_professor", source = "professor.id")
    DisciplinaDTO toDto(Disciplina disciplina);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "nomeDisciplina", source = "nome")
    @Mapping(target = "descricaoDisciplina", source = "descricao")
    @Mapping(target = "codigoDisciplina", source = "codigo")
    @Mapping(target = "cargaHoraria", source = "cargaHoraria")
    Disciplina toEntity(DisciplinaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "nomeDisciplina", source = "nome")
    @Mapping(target = "descricaoDisciplina", source = "descricao")
    @Mapping(target = "codigoDisciplina", source = "codigo")
    @Mapping(target = "cargaHoraria", source = "cargaHoraria")
    void updateEntityFromDto(DisciplinaDTO dto, @MappingTarget Disciplina disciplina);
}
