package org.Pratica_SpringBoot.Services;

import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Models.Mappers.CursoMapper;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    public CursoService(CursoRepository cursoRepository, CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
    }

    public CursoDTO criar(CursoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de curso não pode ser nulo");
        }

        Curso curso = cursoMapper.toEntity(dto);
        return cursoMapper.toDto(cursoRepository.save(curso));
    }

    public Page<CursoDTO> listarTodos(Pageable pageable) {
        return cursoRepository.findAll(pageable).map(cursoMapper::toDto);
    }

    public CursoDTO buscarPorId(Long id) {
        return cursoMapper.toDto(buscarEntidadePorId(id));
    }

    public CursoDTO atualizar(Long id, CursoDTO dto) {
        Curso curso = buscarEntidadePorId(id);
        cursoMapper.updateEntityFromDto(dto, curso);
        return cursoMapper.toDto(cursoRepository.save(curso));
    }

    public void deletar(Long id) {
        cursoRepository.delete(buscarEntidadePorId(id));
    }

    private Curso buscarEntidadePorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Curso não encontrado: " + id));
    }

}