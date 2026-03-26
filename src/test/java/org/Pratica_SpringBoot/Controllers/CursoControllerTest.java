package org.Pratica_SpringBoot.Controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais;
import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Services.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    private MockMvc mockMvc;

    // -------------------------------------------------------------------------
    // Payloads JSON reutilizáveis
    // -------------------------------------------------------------------------

    private static final String PAYLOAD_VALIDO = """
            {"codigo":"ADS","nome":"Análise e Desenvolvimento de Sistemas","descricao":"Curso de tecnologia"}
            """;

    private static final String PAYLOAD_ATUALIZADO = """
            {"codigo":"ADS2","nome":"Novo nome","descricao":"Nova descrição"}
            """;

    private static final String PAYLOAD_CODIGO_VAZIO = """
            {"codigo":"","nome":"Análise","descricao":"Curso"}
            """;

    private static final String PAYLOAD_NOME_VAZIO = """
            {"codigo":"ADS","nome":"","descricao":"Curso"}
            """;

    private static final String PAYLOAD_NOME_CURTO = """
            {"codigo":"ADS","nome":"AB","descricao":"Curso"}
            """;

    private static final String PAYLOAD_CODIGO_LONGO = """
            {"codigo":"ESTE_CODIGO_TEM_MAIS_DE_VINTE_CARACTERES","nome":"Análise","descricao":"Curso"}
            """;

    private static final String PAYLOAD_DESCRICAO_LONGA =
            "{\"codigo\":\"ADS\",\"nome\":\"Análise\",\"descricao\":\"" + "X".repeat(501) + "\"}";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CursoController(cursoService))
                                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(new SortHandlerMethodArgumentResolver()))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    // =========================================================================
    // POST /api/cursos
    // =========================================================================

    @Test
    void criarDeveRetornar201ComDadosDoDTO() throws Exception {
        CursoDTO resposta = cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia");
        when(cursoService.criar(any(CursoDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_curso").value(1L))
                .andExpect(jsonPath("$.codigo").value("ADS"))
                .andExpect(jsonPath("$.nome").value("Análise e Desenvolvimento de Sistemas"))
                .andExpect(jsonPath("$.descricao").value("Curso de tecnologia"));

        verify(cursoService).criar(any(CursoDTO.class));
    }

    @Test
    void criarSemCodigoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_CODIGO_VAZIO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(cursoService);
    }

    @Test
    void criarSemNomeDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_NOME_VAZIO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(cursoService);
    }

    @Test
    void criarComNomeMuitoCurtoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_NOME_CURTO))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    @Test
    void criarComCodigoAcimaDoLimiteDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_CODIGO_LONGO))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    @Test
    void criarComDescricaoAcimaDoLimiteDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_DESCRICAO_LONGA))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    @Test
    void criarSemBodyDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

        // =========================================================================
        // GET /api/cursos
        // =========================================================================

    @Test
    void listarTodosDeveRetornar200ComListaPopulada() throws Exception {
        when(cursoService.listarTodos(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(
                cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia"),
                cursoDTO(2L, "MAT", "Matemática", null)), PageRequest.of(0, 10), 2));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id_curso").value(1L))
                .andExpect(jsonPath("$.content[0].codigo").value("ADS"))
                .andExpect(jsonPath("$.content[1].id_curso").value(2L))
                .andExpect(jsonPath("$.content[1].codigo").value("MAT"));

        verify(cursoService).listarTodos(any(Pageable.class));
    }

    @Test
    void listarTodosDeveRetornar200ComListaVazia() throws Exception {
        when(cursoService.listarTodos(any(Pageable.class))).thenReturn(Page.empty(PageRequest.of(0, 10)));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    // =========================================================================
    // GET /api/cursos/{id}
    // =========================================================================

    @Test
    void buscarPorIdDeveRetornar200QuandoExiste() throws Exception {
        when(cursoService.buscarPorId(1L))
                .thenReturn(cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia"));

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_curso").value(1L))
                .andExpect(jsonPath("$.codigo").value("ADS"));

        verify(cursoService).buscarPorId(eq(1L));
    }

    @Test
    void buscarPorIdDeveRetornar404QuandoNaoExiste() throws Exception {
        when(cursoService.buscarPorId(99L))
                .thenThrow(new NoSuchElementException("Curso não encontrado: 99"));

        mockMvc.perform(get("/api/cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.titulo").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detalhe").value("Curso não encontrado: 99"));

        verify(cursoService).buscarPorId(eq(99L));
    }

    @Test
    void buscarPorIdComTipoInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(get("/api/cursos/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.titulo").value("Parâmetro de URL inválido"));

        verifyNoInteractions(cursoService);
    }

    // =========================================================================
    // PUT /api/cursos/{id}
    // =========================================================================

    @Test
    void atualizarDeveRetornar200ComDadosAtualizados() throws Exception {
        CursoDTO resposta = cursoDTO(1L, "ADS2", "Novo nome", "Nova descrição");
        when(cursoService.atualizar(eq(1L), any(CursoDTO.class))).thenReturn(resposta);

        mockMvc.perform(put("/api/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_ATUALIZADO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_curso").value(1L))
                .andExpect(jsonPath("$.codigo").value("ADS2"))
                .andExpect(jsonPath("$.nome").value("Novo nome"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"));

        verify(cursoService).atualizar(eq(1L), any(CursoDTO.class));
    }

    @Test
    void atualizarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(put("/api/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_CODIGO_VAZIO))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    @Test
    void atualizarCursoInexistenteDeveRetornar404() throws Exception {
        when(cursoService.atualizar(eq(99L), any(CursoDTO.class)))
                .thenThrow(new NoSuchElementException("Curso não encontrado: 99"));

        mockMvc.perform(put("/api/cursos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_ATUALIZADO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detalhe").value("Curso não encontrado: 99"));
    }

    // =========================================================================
    // DELETE /api/cursos/{id}
    // =========================================================================

    @Test
    void deletarDeveRetornar204QuandoExiste() throws Exception {
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService).deletar(1L);
    }

    @Test
    void deletarCursoInexistenteDeveRetornar404() throws Exception {
        org.mockito.Mockito.doThrow(new NoSuchElementException("Curso não encontrado: 99"))
                .when(cursoService).deletar(99L);

        mockMvc.perform(delete("/api/cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detalhe").value("Curso não encontrado: 99"));

        verify(cursoService).deletar(99L);
    }

    @Test
    void deletarComTipoInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(delete("/api/cursos/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    // =========================================================================
    // Método auxiliar
    // =========================================================================

    private CursoDTO cursoDTO(Long id, String codigo, String nome, String descricao) {
        CursoDTO dto = new CursoDTO();
        dto.setId_curso(id);
        dto.setCodigo(codigo);
        dto.setNome(nome);
        dto.setDescricao(descricao);
        return dto;
    }
}