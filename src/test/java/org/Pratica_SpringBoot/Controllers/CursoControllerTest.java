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
import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Services.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CursoController(cursoService))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    @Test
    void criarDeveRetornar201() throws Exception {
        CursoDTO resposta = cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia");
        when(cursoService.criar(any(CursoDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content("""
                                {"codigo":"ADS","nome":"Análise e Desenvolvimento de Sistemas","descricao":"Curso de tecnologia"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_curso").value(1L))
                .andExpect(jsonPath("$.codigo").value("ADS"));

        verify(cursoService).criar(any(CursoDTO.class));
    }

    @Test
    void listarTodosDeveRetornar200() throws Exception {
        when(cursoService.listarTodos()).thenReturn(List.of(cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia")));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_curso").value(1L))
                .andExpect(jsonPath("$[0].codigo").value("ADS"));
    }

    @Test
    void buscarPorIdDeveRetornar200() throws Exception {
        when(cursoService.buscarPorId(1L)).thenReturn(cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia"));

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_curso").value(1L))
                .andExpect(jsonPath("$.codigo").value("ADS"));

        verify(cursoService).buscarPorId(eq(1L));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        CursoDTO resposta = cursoDTO(1L, "ADS2", "Novo nome", "Nova descrição");
        when(cursoService.atualizar(eq(1L), any(CursoDTO.class))).thenReturn(resposta);

        mockMvc.perform(put("/api/cursos/1")
                        .contentType("application/json")
                        .content("""
                                {"codigo":"ADS2","nome":"Novo nome","descricao":"Nova descrição"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("ADS2"));
    }

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService).deletar(1L);
    }

    @Test
    void criarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content("""
                                {"codigo":"","nome":"","descricao":"Curso"}
                                """))
                .andExpect(status().isBadRequest());
    }

    private CursoDTO cursoDTO(Long id, String codigo, String nome, String descricao) {
        CursoDTO dto = new CursoDTO();
        dto.setId_curso(id);
        dto.setCodigo(codigo);
        dto.setNome(nome);
        dto.setDescricao(descricao);
        return dto;
    }
}