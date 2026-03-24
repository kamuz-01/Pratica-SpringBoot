package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final ProfessorRepository professorRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository,
            ProfessorRepository professorRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.professorRepository = professorRepository;
    }

    public DisciplinaDTO criar(DisciplinaDTO dto) {
        Disciplina disciplina = toEntity(dto);
        return toDTO(disciplinaRepository.save(disciplina));
    }

    public List<DisciplinaDTO> listarTodos() {
        return disciplinaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public DisciplinaDTO buscarPorId(Long id) {
        return toDTO(buscarEntidadePorId(id));
    }

    public DisciplinaDTO atualizar(Long id, DisciplinaDTO dto) {
        Disciplina disciplina = buscarEntidadePorId(id);
        atualizarEntidade(disciplina, dto);
        return toDTO(disciplinaRepository.save(disciplina));
    }

    public void deletar(Long id) {
        disciplinaRepository.delete(buscarEntidadePorId(id));
    }

    private Disciplina buscarEntidadePorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada: " + id));
    }

    private Disciplina toEntity(DisciplinaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de disciplina não pode ser nulo");
        }

        Disciplina disciplina = new Disciplina();
        atualizarEntidade(disciplina, dto);
        return disciplina;
    }

    private void atualizarEntidade(Disciplina disciplina, DisciplinaDTO dto) {
        Curso curso = cursoRepository.findById(dto.getId_curso())
                .orElseThrow(() -> new NoSuchElementException("Curso não encontrado: " + dto.getId_curso()));
        Professor professor = professorRepository.findById(dto.getId_professor())
                .orElseThrow(() -> new NoSuchElementException("Professor não encontrado: " + dto.getId_professor()));

        disciplina.setId_disciplina(dto.getId_disciplina());
        disciplina.setNomeDisciplina(dto.getNome());
        disciplina.setDescricaoDisciplina(dto.getDescricao());
        disciplina.setCodigoDisciplina(dto.getCodigo());
        disciplina.setCargaHoraria(dto.getCargaHoraria());
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
    }

    private DisciplinaDTO toDTO(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId_disciplina(disciplina.getId_disciplina());
        dto.setNome(disciplina.getNomeDisciplina());
        dto.setDescricao(disciplina.getDescricaoDisciplina());
        dto.setCodigo(disciplina.getCodigoDisciplina());
        dto.setCargaHoraria(disciplina.getCargaHoraria());
        dto.setId_curso(disciplina.getCurso() != null ? disciplina.getCurso().getIdCurso() : null);
        dto.setId_professor(disciplina.getProfessor() != null ? disciplina.getProfessor().getId() : null);
        return dto;
    }
}