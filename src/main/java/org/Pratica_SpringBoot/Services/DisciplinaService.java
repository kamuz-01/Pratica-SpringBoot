package org.Pratica_SpringBoot.Services;

import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Models.Mappers.DisciplinaMapper;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final DisciplinaMapper disciplinaMapper;
    private final CursoRepository cursoRepository;
    private final ProfessorRepository professorRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, DisciplinaMapper disciplinaMapper,
            CursoRepository cursoRepository,
            ProfessorRepository professorRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaMapper = disciplinaMapper;
        this.cursoRepository = cursoRepository;
        this.professorRepository = professorRepository;
    }

    public DisciplinaDTO criar(DisciplinaDTO dto) {
        Disciplina disciplina = disciplinaMapper.toEntity(dto);
        atualizarRelacionamentos(disciplina, dto);
        return disciplinaMapper.toDto(disciplinaRepository.save(disciplina));
    }

    public Page<DisciplinaDTO> listarTodos(Pageable pageable) {
        return disciplinaRepository.findAll(pageable).map(disciplinaMapper::toDto);
    }

    public DisciplinaDTO buscarPorId(Long id) {
        return disciplinaMapper.toDto(buscarEntidadePorId(id));
    }

    public DisciplinaDTO atualizar(Long id, DisciplinaDTO dto) {
        Disciplina disciplina = buscarEntidadePorId(id);
        disciplinaMapper.updateEntityFromDto(dto, disciplina);
        atualizarRelacionamentos(disciplina, dto);
        return disciplinaMapper.toDto(disciplinaRepository.save(disciplina));
    }

    public void deletar(Long id) {
        disciplinaRepository.delete(buscarEntidadePorId(id));
    }

    private Disciplina buscarEntidadePorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada: " + id));
    }

    private void atualizarRelacionamentos(Disciplina disciplina, DisciplinaDTO dto) {
        Curso curso = cursoRepository.findById(dto.getId_curso())
                .orElseThrow(() -> new NoSuchElementException("Curso não encontrado: " + dto.getId_curso()));
        Professor professor = professorRepository.findById(dto.getId_professor())
                .orElseThrow(() -> new NoSuchElementException("Professor não encontrado: " + dto.getId_professor()));

        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
    }
}