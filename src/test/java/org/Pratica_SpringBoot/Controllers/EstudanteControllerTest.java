package org.Pratica_SpringBoot.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais;
import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Services.EstudanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EstudanteControllerTest {

    @Mock
    private EstudanteService estudanteService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EstudanteController(estudanteService))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    @Test
    void criarDeveRetornar201() throws Exception {
        EstudanteDTO resposta = estudanteDTO(1L, "123.456.789-09", "Ana", "Silva", "20240001");
        when(estudanteService.criar(any(EstudanteDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/estudantes")
                        .contentType("application/json")
                        .content("""
                                {"nome":"Ana","sobrenome":"Silva","cpf":"123.456.789-09","dataNascimento":"2000-01-01","cidade":"São Paulo","estado":"SP","paisOrigem":"Brasil","telefone":"11999999999","email":"ana@email.com","senha":"senhaSegura1","matricula":"20240001"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L))
                .andExpect(jsonPath("$.matricula").value("20240001"));
    }

    @Test
    void criarComImagemDeveRetornar201() throws Exception {
        EstudanteDTO resposta = estudanteDTO(1L, "123.456.789-09", "Ana", "Silva", "20240001");
        when(estudanteService.criar(any(EstudanteDTO.class), any())).thenReturn(resposta);

        MockMultipartFile dados = new MockMultipartFile("dados", "", "application/json", """
                {"nome":"Ana","sobrenome":"Silva","cpf":"123.456.789-09","dataNascimento":"2000-01-01","cidade":"São Paulo","estado":"SP","paisOrigem":"Brasil","telefone":"11999999999","email":"ana@email.com","senha":"senhaSegura1","matricula":"20240001"}
                """.getBytes());
        MockMultipartFile imagem = new MockMultipartFile("imagemPerfil", "foto.png", "image/png", new byte[] { 1, 2, 3 });

        mockMvc.perform(multipart("/api/estudantes").file(dados).file(imagem))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L));
    }

    @Test
    void listarTodosDeveRetornar200() throws Exception {
        when(estudanteService.listarTodos()).thenReturn(List.of(estudanteDTO(1L, "123.456.789-09", "Ana", "Silva", "20240001")));

        mockMvc.perform(get("/api/estudantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricula").value("20240001"));
    }

    @Test
    void buscarPorIdDeveRetornar200() throws Exception {
        when(estudanteService.buscarPorId(1L)).thenReturn(estudanteDTO(1L, "123.456.789-09", "Ana", "Silva", "20240001"));

        mockMvc.perform(get("/api/estudantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(1L));

        verify(estudanteService).buscarPorId(eq(1L));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        EstudanteController controller = new EstudanteController(estudanteService);
        EstudanteDTO request = estudanteDTO(1L, "123.456.789-10", "Ana Maria", "Silva", "20240002");
        when(estudanteService.atualizar(eq(1L), any(EstudanteDTO.class))).thenReturn(request);

        ResponseEntity<EstudanteDTO> response = controller.atualizar(1L, request);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals("123.456.789-10", response.getBody().getCpf());
    }

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/estudantes/1"))
                .andExpect(status().isNoContent());

        verify(estudanteService).deletar(1L);
    }

    @Test
    void criarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/estudantes")
                        .contentType("application/json")
                        .content("""
                                {"nome":"","sobrenome":"","cpf":"","dataNascimento":null,"cidade":"","estado":"","paisOrigem":"","telefone":"","email":"","senha":"","matricula":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    private EstudanteDTO estudanteDTO(Long id, String cpf, String nome, String sobrenome, String matricula) {
        EstudanteDTO dto = new EstudanteDTO();
        dto.setId_usuario(id);
        dto.setNome(nome);
        dto.setSobrenome(sobrenome);
        dto.setCpf(cpf);
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));
        dto.setCidade("São Paulo");
        dto.setEstado("SP");
        dto.setPaisOrigem("Brasil");
        dto.setTelefone("11999999999");
        dto.setEmail("ana@email.com");
        dto.setSenha("senhaSegura1");
        dto.setMatricula(matricula);
        return dto;
    }
}