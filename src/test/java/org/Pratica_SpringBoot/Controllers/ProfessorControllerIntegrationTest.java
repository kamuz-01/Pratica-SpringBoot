package org.Pratica_SpringBoot.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockPart;

class ProfessorControllerIntegrationTest extends ControllersIntegrationSupport {

    @Test
    void criarComImagemDeveRetornar201() throws Exception {
        stubProfessorCriarImagem();

        MockPart dados = new MockPart("dados", "dados.json", professorDadosBytes());
        dados.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        MockPart imagem = new MockPart("imagemPerfil", "foto.png", new byte[] { 1, 2, 3 });
        imagem.getHeaders().setContentType(org.springframework.http.MediaType.IMAGE_PNG);

        mockMvc.perform(multipart("/api/professores").part(dados).part(imagem))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L))
                .andExpect(jsonPath("$.especialidade").value("Matemática"));
    }
}