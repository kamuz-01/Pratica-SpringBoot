package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Models.Entities.Matricula;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
import org.Pratica_SpringBoot.Repositories.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudanteRepository estudanteRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, EstudanteRepository estudanteRepository,
            DisciplinaRepository disciplinaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.estudanteRepository = estudanteRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public MatriculaDTO criar(MatriculaDTO dto) {
        validarUnicidade(dto.getIdEstudante(), dto.getIdDisciplina(), dto.getSemestre(), null);
        Matricula matricula = toEntity(dto);
        return toDTO(matriculaRepository.save(matricula));
    }

    public List<MatriculaDTO> listarTodos() {
        return matriculaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public MatriculaDTO buscarPorId(Long id) {
        return toDTO(buscarEntidadePorId(id));
    }

    public MatriculaDTO atualizar(Long id, MatriculaDTO dto) {
        Matricula matriculaAtual = buscarEntidadePorId(id);
        validarUnicidade(dto.getIdEstudante(), dto.getIdDisciplina(), dto.getSemestre(), id);
        atualizarEntidade(matriculaAtual, dto);
        return toDTO(matriculaRepository.save(matriculaAtual));
    }

    public void deletar(Long id) {
        matriculaRepository.delete(buscarEntidadePorId(id));
    }

    private void validarUnicidade(Long estudanteId, Long disciplinaId, Integer semestre, Long idIgnorar) {
        matriculaRepository.findByEstudanteIdAndDisciplinaIdAndSemestre(estudanteId, disciplinaId, semestre)
                .filter(matricula -> idIgnorar == null || !matricula.getId().equals(idIgnorar))
                .ifPresent(matricula -> {
                    throw new IllegalArgumentException("Já existe matrícula para este estudante, disciplina e semestre");
                });
    }

    private Matricula buscarEntidadePorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Matrícula não encontrada: " + id));
    }

    private Matricula toEntity(MatriculaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de matrícula não pode ser nulo");
        }

        Matricula matricula = new Matricula();
        atualizarEntidade(matricula, dto);
        return matricula;
    }

    private void atualizarEntidade(Matricula matricula, MatriculaDTO dto) {
        Estudante estudante = estudanteRepository.findById(dto.getIdEstudante())
                .orElseThrow(() -> new NoSuchElementException("Estudante não encontrado: " + dto.getIdEstudante()));
        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada: " + dto.getIdDisciplina()));

        matricula.setId(dto.getId_matricula());
        matricula.setEstudante(estudante);
        matricula.setDisciplina(disciplina);
        matricula.setSemestre(dto.getSemestre());
        matricula.setDataMatricula(dto.getDataMatricula() != null ? dto.getDataMatricula() : matricula.getDataMatricula());
        matricula.setFrequencia(dto.getFrequencia());
        matricula.setNotaFinal(dto.getNotaFinal());
        matricula.setStatus(dto.getStatus());
    }

    private MatriculaDTO toDTO(Matricula matricula) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId_matricula(matricula.getId());
        dto.setIdEstudante(matricula.getEstudante() != null ? matricula.getEstudante().getId() : null);
        dto.setIdDisciplina(matricula.getDisciplina() != null ? matricula.getDisciplina().getId() : null);
        dto.setSemestre(matricula.getSemestre());
        dto.setDataMatricula(matricula.getDataMatricula());
        dto.setFrequencia(matricula.getFrequencia());
        dto.setNotaFinal(matricula.getNotaFinal());
        dto.setStatus(matricula.getStatus());
        return dto;
    }
}