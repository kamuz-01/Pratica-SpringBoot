package org.Pratica_SpringBoot.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class CursoControllerIntegrationTest extends ControllersIntegrationSupport {

    @Test
    void listarDeveRetornar200() throws Exception {
        stubCursoListarPadrao();

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("ADS"));
    }

    @Test
    void buscarInexistenteDeveRetornar404() throws Exception {
        stubCursoNaoEncontrado();

        mockMvc.perform(get("/api/cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.titulo").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detalhe").value("Curso não encontrado: 99"));
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

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());
    }
}