package org.Pratica_SpringBoot.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais;
import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Services.DisciplinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DisciplinaControllerTest {

    @Mock
    private DisciplinaService disciplinaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DisciplinaController(disciplinaService))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    @Test
    void criarDeveRetornar201() throws Exception {
        DisciplinaDTO resposta = disciplinaDTO(1L, "Programação", "Base", "PROG101", 80, 2L, 3L);
        when(disciplinaService.criar(any(DisciplinaDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/disciplinas")
                        .contentType("application/json")
                        .content("""
                                {"nome":"Programação","descricao":"Base","codigo":"PROG101","cargaHoraria":80,"id_curso":2,"id_professor":3}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_disciplina").value(1L))
                .andExpect(jsonPath("$.nome").value("Programação"));
    }

    @Test
    void listarTodosDeveRetornar200() throws Exception {
        when(disciplinaService.listarTodos()).thenReturn(List.of(disciplinaDTO(1L, "Programação", "Base", "PROG101", 80, 2L, 3L)));

        mockMvc.perform(get("/api/disciplinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_disciplina").value(1L));
    }

    @Test
    void buscarPorIdDeveRetornar200() throws Exception {
        when(disciplinaService.buscarPorId(1L)).thenReturn(disciplinaDTO(1L, "Programação", "Base", "PROG101", 80, 2L, 3L));

        mockMvc.perform(get("/api/disciplinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Programação"));

        verify(disciplinaService).buscarPorId(eq(1L));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        DisciplinaDTO resposta = disciplinaDTO(1L, "Programação Avançada", "Novo", "PROG102", 100, 2L, 3L);
        when(disciplinaService.atualizar(eq(1L), any(DisciplinaDTO.class))).thenReturn(resposta);

        mockMvc.perform(put("/api/disciplinas/1")
                        .contentType("application/json")
                        .content("""
                                {"nome":"Programação Avançada","descricao":"Novo","codigo":"PROG102","cargaHoraria":100,"id_curso":2,"id_professor":3}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PROG102"));
    }

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/disciplinas/1"))
                .andExpect(status().isNoContent());

        verify(disciplinaService).deletar(1L);
    }

    @Test
    void criarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/disciplinas")
                        .contentType("application/json")
                        .content("""
                                {"nome":"","codigo":"","cargaHoraria":10,"id_curso":null,"id_professor":null}
                                """))
                .andExpect(status().isBadRequest());
    }

    private DisciplinaDTO disciplinaDTO(Long id, String nome, String descricao, String codigo, Integer cargaHoraria,
            Long idCurso, Long idProfessor) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId_disciplina(id);
        dto.setNome(nome);
        dto.setDescricao(descricao);
        dto.setCodigo(codigo);
        dto.setCargaHoraria(cargaHoraria);
        dto.setId_curso(idCurso);
        dto.setId_professor(idProfessor);
        return dto;
    }
}