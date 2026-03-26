package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.Pratica_SpringBoot.Models.Mappers.CursoMapper;
import org.Pratica_SpringBoot.Repositories.CursoRepository;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        cursoService = new CursoService(cursoRepository, Mappers.getMapper(CursoMapper.class));
    }

    @Test
    void criarDeveSalvarCursoERetornarDTO() {
        CursoDTO cursoDTO = cursoDTO("ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia");
        Curso cursoSalvo = curso("ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia", 1L);
        when(cursoRepository.save(argThat((Curso curso) -> true))).thenReturn(cursoSalvo);

        CursoDTO resultado = cursoService.criar(cursoDTO);

        assertEquals(1L, resultado.getId_curso());
        assertEquals("ADS", resultado.getCodigo());
        assertEquals("Análise e Desenvolvimento de Sistemas", resultado.getNome());
        assertEquals("Curso de tecnologia", resultado.getDescricao());

        ArgumentCaptor<Curso> captor = ArgumentCaptor.forClass(Curso.class);
        verify(cursoRepository).save(captor.capture());
        assertEquals("ADS", captor.getValue().getCodigoCurso());
    }

    @Test
    void listarTodosDeveConverterEntidadesEmDTOs() {
        when(cursoRepository.findAll(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(
            new PageImpl<>(List.of(
                curso("ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia", 1L),
                curso("MAT", "Matemática", null, 2L)), PageRequest.of(0, 10), 2));

        Page<CursoDTO> resultado = cursoService.listarTodos(PageRequest.of(0, 10));

        assertEquals(2, resultado.getTotalElements());
        assertEquals("ADS", resultado.getContent().get(0).getCodigo());
        assertEquals("MAT", resultado.getContent().get(1).getCodigo());
    }

    @Test
    void buscarPorIdDeveRetornarDTOQuandoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso("ADS", "Análise e Desenvolvimento de Sistemas", null, 1L)));

        CursoDTO resultado = cursoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId_curso());
        assertEquals("ADS", resultado.getCodigo());
    }

    @Test
    void buscarPorIdDeveLancarExcecaoQuandoNaoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> cursoService.buscarPorId(1L));
        assertEquals("Curso não encontrado: 1", exception.getMessage());
    }

    @Test
    void atualizarDevePersistirNovosDados() {
        Curso existente = curso("ADS", "Antigo", "Antiga", 1L);
        Curso atualizado = curso("ADS2", "Novo nome", "Nova descrição", 1L);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(argThat((Curso curso) -> true))).thenReturn(atualizado);

        CursoDTO novoDTO = cursoDTO("ADS2", "Novo nome", "Nova descrição");
        CursoDTO resultado = cursoService.atualizar(1L, novoDTO);

        assertEquals("ADS2", resultado.getCodigo());
        assertEquals("Novo nome", resultado.getNome());
        assertEquals("Nova descrição", resultado.getDescricao());

        ArgumentCaptor<Curso> captor = ArgumentCaptor.forClass(Curso.class);
        verify(cursoRepository).save(captor.capture());
        assertEquals("ADS2", captor.getValue().getCodigoCurso());
    }

    @Test
    void deletarDeveRemoverCursoExistente() {
        Curso existente = curso("ADS", "Análise e Desenvolvimento de Sistemas", null, 1L);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(existente));

        cursoService.deletar(1L);

        ArgumentCaptor<Curso> captor = ArgumentCaptor.forClass(Curso.class);
        verify(cursoRepository).delete(captor.capture());
        assertEquals(existente, captor.getValue());
    }

    @Test
    void criarComDtoNuloDeveFalhar() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cursoService.criar(null));
        assertEquals("DTO de curso não pode ser nulo", exception.getMessage());
    }

    private Curso curso(String codigo, String nome, String descricao, Long id) {
        Curso curso = new Curso();
        curso.setIdCurso(id);
        curso.setCodigoCurso(codigo);
        curso.setNomeCurso(nome);
        curso.setDescricaoCurso(descricao);
        return curso;
    }

    private CursoDTO cursoDTO(String codigo, String nome, String descricao) {
        CursoDTO dto = new CursoDTO();
        dto.setCodigo(codigo);
        dto.setNome(nome);
        dto.setDescricao(descricao);
        return dto;
    }
}