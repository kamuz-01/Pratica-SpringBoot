package org.Pratica_SpringBoot.Models.Mappers;

import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CursoMapper {

    @Mapping(target = "codigo", source = "codigoCurso")
    @Mapping(target = "nome", source = "nomeCurso")
    @Mapping(target = "descricao", source = "descricaoCurso")
    @Mapping(target = "id_curso", source = "idCurso")
    CursoDTO toDto(Curso curso);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "codigoCurso", source = "codigo")
    @Mapping(target = "nomeCurso", source = "nome")
    @Mapping(target = "descricaoCurso", source = "descricao")
    Curso toEntity(CursoDTO dto);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "codigoCurso", source = "codigo")
    @Mapping(target = "nomeCurso", source = "nome")
    @Mapping(target = "descricaoCurso", source = "descricao")
    void updateEntityFromDto(CursoDTO dto, @MappingTarget Curso curso);
}
