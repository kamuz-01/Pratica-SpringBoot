package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Models.Mappers.DisciplinaMapper;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
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
class DisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    private DisciplinaService disciplinaService;

    @BeforeEach
    void setUp() {
        disciplinaService = new DisciplinaService(disciplinaRepository, Mappers.getMapper(DisciplinaMapper.class),
                cursoRepository, professorRepository);
    }

    @Test
    void criarDeveSalvarDisciplina() {
        DisciplinaDTO disciplinaDTO = disciplinaDTO("Programação", "Base da programação", "PROG101", 80, 1L, 2L);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso(1L)));
        when(professorRepository.findById(2L)).thenReturn(Optional.of(professor(2L)));
        when(disciplinaRepository.save(argThat((Disciplina disciplina) -> true))).thenAnswer(invocation -> invocation.getArgument(0));

        DisciplinaDTO resultado = disciplinaService.criar(disciplinaDTO);

        assertEquals("Programação", resultado.getNome());
        assertEquals(1L, resultado.getId_curso());
        assertEquals(2L, resultado.getId_professor());

        ArgumentCaptor<Disciplina> captor = ArgumentCaptor.forClass(Disciplina.class);
        verify(disciplinaRepository).save(captor.capture());
        assertEquals("Programação", captor.getValue().getNomeDisciplina());
    }

    @Test
    void criarDeveLancarQuandoCursoNaoExiste() {
        DisciplinaDTO disciplinaDTO = disciplinaDTO("Programação", "Base da programação", "PROG101", 80, 1L, 2L);
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> disciplinaService.criar(disciplinaDTO));
        assertEquals("Curso não encontrado: 1", exception.getMessage());
    }

    @Test
    void listarTodosDeveConverterEntidades() {
        Disciplina disciplina = disciplina(10L, "Programação", 1L, 2L);
        when(disciplinaRepository.findAll(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(disciplina), PageRequest.of(0, 10), 1));

        Page<DisciplinaDTO> resultado = disciplinaService.listarTodos(PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals(10L, resultado.getContent().get(0).getId_disciplina());
    }

    @Test
    void atualizarDevePersistirAlteracoes() {
        Disciplina existente = disciplina(10L, "Antiga", 1L, 2L);
        DisciplinaDTO disciplinaDTO = disciplinaDTO("Programação", "Base da programação", "PROG101", 80, 1L, 2L);
        disciplinaDTO.setId_disciplina(10L);
        when(disciplinaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso(1L)));
        when(professorRepository.findById(2L)).thenReturn(Optional.of(professor(2L)));
        when(disciplinaRepository.save(argThat((Disciplina disciplina) -> true))).thenAnswer(invocation -> invocation.getArgument(0));

        DisciplinaDTO resultado = disciplinaService.atualizar(10L, disciplinaDTO);

        assertEquals(10L, resultado.getId_disciplina());
        assertEquals("Programação", resultado.getNome());
    }

    @Test
    void deletarDeveRemoverDisciplina() {
        Disciplina existente = disciplina(10L, "Programação", 1L, 2L);
        when(disciplinaRepository.findById(10L)).thenReturn(Optional.of(existente));

        disciplinaService.deletar(10L);

        ArgumentCaptor<Disciplina> captor = ArgumentCaptor.forClass(Disciplina.class);
        verify(disciplinaRepository).delete(captor.capture());
        assertEquals(existente, captor.getValue());
    }

    private Curso curso(Long id) {
        Curso curso = new Curso();
        curso.setId(id);
        return curso;
    }

    private Professor professor(Long id) {
        Professor professor = new Professor();
        professor.setId(id);
        return professor;
    }

    private Disciplina disciplina(Long id, String nome, Long cursoId, Long professorId) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);
        disciplina.setNomeDisciplina(nome);
        disciplina.setDescricaoDisciplina("Descricao");
        disciplina.setCodigoDisciplina("COD");
        disciplina.setCargaHoraria(80);
        disciplina.setCurso(curso(cursoId));
        disciplina.setProfessor(professor(professorId));
        return disciplina;
    }

    private DisciplinaDTO disciplinaDTO(String nome, String descricao, String codigo, Integer cargaHoraria,
            Long idCurso, Long idProfessor) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setNome(nome);
        dto.setDescricao(descricao);
        dto.setCodigo(codigo);
        dto.setCargaHoraria(cargaHoraria);
        dto.setId_curso(idCurso);
        dto.setId_professor(idProfessor);
        return dto;
    }
}