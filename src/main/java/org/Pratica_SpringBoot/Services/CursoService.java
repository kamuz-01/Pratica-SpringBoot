package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public CursoDTO criar(CursoDTO dto) {
        Curso curso = toEntity(dto);
        return toDTO(cursoRepository.save(curso));
    }

    public List<CursoDTO> listarTodos() {
        return cursoRepository.findAll().stream().map(this::toDTO).toList();
    }

    public CursoDTO buscarPorId(Long id) {
        return toDTO(buscarEntidadePorId(id));
    }

    public CursoDTO atualizar(Long id, CursoDTO dto) {
        Curso curso = buscarEntidadePorId(id);
        curso.setCodigoCurso(dto.getCodigo());
        curso.setNomeCurso(dto.getNome());
        curso.setDescricaoCurso(dto.getDescricao());
        return toDTO(cursoRepository.save(curso));
    }

    public void deletar(Long id) {
        cursoRepository.delete(buscarEntidadePorId(id));
    }

    private Curso buscarEntidadePorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Curso não encontrado: " + id));
    }

    private Curso toEntity(CursoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de curso não pode ser nulo");
        }

        Curso curso = new Curso();
        curso.setCodigoCurso(dto.getCodigo());
        curso.setNomeCurso(dto.getNome());
        curso.setDescricaoCurso(dto.getDescricao());
        return curso;
    }

    private CursoDTO toDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId_curso(curso.getIdCurso());
        dto.setCodigo(curso.getCodigoCurso());
        dto.setNome(curso.getNomeCurso());
        dto.setDescricao(curso.getDescricaoCurso());
        return dto;
    }
}