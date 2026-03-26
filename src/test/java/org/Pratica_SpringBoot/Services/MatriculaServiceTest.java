package org.Pratica_SpringBoot.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Models.Entities.Matricula;
import org.Pratica_SpringBoot.Models.Enums.StatusMatricula;
import org.Pratica_SpringBoot.Models.Mappers.MatriculaMapper;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
import org.Pratica_SpringBoot.Repositories.MatriculaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    private MatriculaService matriculaService;

    @BeforeEach
    void setUp() {
        matriculaService = new MatriculaService(matriculaRepository, Mappers.getMapper(MatriculaMapper.class),
                estudanteRepository, disciplinaRepository);
    }

    @Test
    void criarDeveSalvarMatricula() {
        MatriculaDTO matriculaDTO = matriculaDTO(1L, 2L, 1, LocalDate.of(2026, 1, 15), 90.0, 8.5, StatusMatricula.ATIVA);
        when(matriculaRepository.findByEstudanteIdAndDisciplinaIdAndSemestre(1L, 2L, 1)).thenReturn(Optional.empty());
        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(estudante(1L)));
        when(disciplinaRepository.findById(2L)).thenReturn(Optional.of(disciplina(2L)));
        when(matriculaRepository.save(argThat((Matricula matricula) -> true))).thenAnswer(invocation -> invocation.getArgument(0));

        MatriculaDTO resultado = matriculaService.criar(matriculaDTO);

        assertEquals(1L, resultado.getIdEstudante());
        assertEquals(2L, resultado.getIdDisciplina());
        assertEquals(StatusMatricula.ATIVA, resultado.getStatus());
    }

    @Test
    void criarDeveLancarQuandoJaExisteMatriculaIgual() {
        MatriculaDTO matriculaDTO = matriculaDTO(1L, 2L, 1, LocalDate.of(2026, 1, 15), 90.0, 8.5, StatusMatricula.ATIVA);
        when(matriculaRepository.findByEstudanteIdAndDisciplinaIdAndSemestre(1L, 2L, 1))
                .thenReturn(Optional.of(matricula(10L, 1L, 2L)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> matriculaService.criar(matriculaDTO));
        assertEquals("Já existe matrícula para este estudante, disciplina e semestre", exception.getMessage());
    }

    @Test
    void listarTodosDeveConverterEntidades() {
        when(matriculaRepository.findAll(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(matricula(10L, 1L, 2L)), PageRequest.of(0, 10), 1));

        Page<MatriculaDTO> resultado = matriculaService.listarTodos(PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals(10L, resultado.getContent().get(0).getId_matricula());
    }

    @Test
    void atualizarDevePersistirAlteracoes() {
        Matricula existente = matricula(10L, 1L, 2L);
        MatriculaDTO novoDTO = matriculaDTO(1L, 2L, 2, LocalDate.of(2026, 2, 15), 95.0, 9.0, StatusMatricula.CONCLUIDA);
        when(matriculaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(matriculaRepository.findByEstudanteIdAndDisciplinaIdAndSemestre(1L, 2L, 2)).thenReturn(Optional.empty());
        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(estudante(1L)));
        when(disciplinaRepository.findById(2L)).thenReturn(Optional.of(disciplina(2L)));
        when(matriculaRepository.save(argThat((Matricula matricula) -> true))).thenAnswer(invocation -> invocation.getArgument(0));

        MatriculaDTO resultado = matriculaService.atualizar(10L, novoDTO);

        assertEquals(2, resultado.getSemestre());
        assertEquals(StatusMatricula.CONCLUIDA, resultado.getStatus());
    }

    @Test
    void buscarPorIdDeveLancarQuandoNaoExiste() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> matriculaService.buscarPorId(1L));
        assertEquals("Matrícula não encontrada: 1", exception.getMessage());
    }

    @Test
    void deletarDeveRemoverMatricula() {
        Matricula existente = matricula(10L, 1L, 2L);
        when(matriculaRepository.findById(10L)).thenReturn(Optional.of(existente));

        matriculaService.deletar(10L);

        ArgumentCaptor<Matricula> captor = ArgumentCaptor.forClass(Matricula.class);
        verify(matriculaRepository).delete(captor.capture());
        assertEquals(existente, captor.getValue());
    }

    private Estudante estudante(Long id) {
        Estudante estudante = new Estudante();
        estudante.setId(id);
        return estudante;
    }

    private Disciplina disciplina(Long id) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);
        return disciplina;
    }

    private Matricula matricula(Long id, Long estudanteId, Long disciplinaId) {
        Matricula matricula = new Matricula();
        matricula.setId(id);
        matricula.setEstudante(estudante(estudanteId));
        matricula.setDisciplina(disciplina(disciplinaId));
        matricula.setSemestre(1);
        matricula.setDataMatricula(LocalDate.of(2026, 1, 15));
        matricula.setFrequencia(90.0);
        matricula.setNotaFinal(8.5);
        matricula.setStatus(StatusMatricula.ATIVA);
        return matricula;
    }

    private MatriculaDTO matriculaDTO(Long estudanteId, Long disciplinaId, Integer semestre, LocalDate dataMatricula,
            Double frequencia, Double notaFinal, StatusMatricula status) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setIdEstudante(estudanteId);
        dto.setIdDisciplina(disciplinaId);
        dto.setSemestre(semestre);
        dto.setDataMatricula(dataMatricula);
        dto.setFrequencia(frequencia);
        dto.setNotaFinal(notaFinal);
        dto.setStatus(status);
        return dto;
    }
}