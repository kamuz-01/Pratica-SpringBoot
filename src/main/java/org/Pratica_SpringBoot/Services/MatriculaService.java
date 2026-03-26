package org.Pratica_SpringBoot.Services;

import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Models.Entities.Matricula;
import org.Pratica_SpringBoot.Models.Mappers.MatriculaMapper;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
import org.Pratica_SpringBoot.Repositories.MatriculaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper;
    private final EstudanteRepository estudanteRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, MatriculaMapper matriculaMapper,
            EstudanteRepository estudanteRepository,
            DisciplinaRepository disciplinaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.matriculaMapper = matriculaMapper;
        this.estudanteRepository = estudanteRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public MatriculaDTO criar(MatriculaDTO dto) {
        validarUnicidade(dto.getIdEstudante(), dto.getIdDisciplina(), dto.getSemestre(), null);
        Matricula matricula = matriculaMapper.toEntity(dto);
        atualizarRelacionamentos(matricula, dto);
        return matriculaMapper.toDto(matriculaRepository.save(matricula));
    }

    public Page<MatriculaDTO> listarTodos(Pageable pageable) {
        return matriculaRepository.findAll(pageable).map(matriculaMapper::toDto);
    }

    public MatriculaDTO buscarPorId(Long id) {
        return matriculaMapper.toDto(buscarEntidadePorId(id));
    }

    public MatriculaDTO atualizar(Long id, MatriculaDTO dto) {
        Matricula matriculaAtual = buscarEntidadePorId(id);
        validarUnicidade(dto.getIdEstudante(), dto.getIdDisciplina(), dto.getSemestre(), id);
        matriculaMapper.updateEntityFromDto(dto, matriculaAtual);
        atualizarRelacionamentos(matriculaAtual, dto);
        if (dto.getDataMatricula() != null) {
            matriculaAtual.setDataMatricula(dto.getDataMatricula());
        }
        return matriculaMapper.toDto(matriculaRepository.save(matriculaAtual));
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

    private void atualizarRelacionamentos(Matricula matricula, MatriculaDTO dto) {
        Estudante estudante = estudanteRepository.findById(dto.getIdEstudante())
                .orElseThrow(() -> new NoSuchElementException("Estudante não encontrado: " + dto.getIdEstudante()));
        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada: " + dto.getIdDisciplina()));

        matricula.setEstudante(estudante);
        matricula.setDisciplina(disciplina);
        matricula.setSemestre(dto.getSemestre());
        matricula.setFrequencia(dto.getFrequencia());
        matricula.setNotaFinal(dto.getNotaFinal());
        matricula.setStatus(dto.getStatus());
    }
}