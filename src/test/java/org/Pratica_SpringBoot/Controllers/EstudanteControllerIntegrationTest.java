package org.Pratica_SpringBoot.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockPart;

class EstudanteControllerIntegrationTest extends ControllersIntegrationSupport {

    @Test
    void criarComImagemDeveRetornar201() throws Exception {
        stubEstudanteCriarImagem();

        MockPart dados = new MockPart("dados", "dados.json", estudanteDadosBytes());
        dados.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        MockPart imagem = new MockPart("imagemPerfil", "foto.png", new byte[] { 1, 2, 3 });
        imagem.getHeaders().setContentType(org.springframework.http.MediaType.IMAGE_PNG);

        mockMvc.perform(multipart("/api/estudantes").part(dados).part(imagem))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L))
                .andExpect(jsonPath("$.matricula").value("20240001"));
    }

    @Test
    void cpfDuplicadoDeveRetornar409() throws Exception {
        stubEstudanteCpfDuplicado();

        mockMvc.perform(post("/api/estudantes")
                        .contentType("application/json")
                        .content("""
                                {"nome":"Ana","sobrenome":"Silva","cpf":"123.456.789-09","dataNascimento":"2000-01-01","cidade":"São Paulo","estado":"SP","paisOrigem":"Brasil","telefone":"11999999999","email":"ana@email.com","senha":"senhaSegura1","matricula":"20240001"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.titulo").value("CPF duplicado"));
    }
}