package org.Pratica_SpringBoot.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class MatriculaControllerIntegrationTest extends ControllersIntegrationSupport {

    @Test
    void criarDeveRetornar201() throws Exception {
        stubMatriculaCriar();

        mockMvc.perform(post("/api/matriculas")
                        .contentType("application/json")
                        .content("""
                                {"idEstudante":1,"idDisciplina":2,"semestre":1,"dataMatricula":"2026-01-15","frequencia":90.0,"notaFinal":8.5,"status":"ATIVA"}
                                """))
                .andExpect(status().isCreated());
    }
}