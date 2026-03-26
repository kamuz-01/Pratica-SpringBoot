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
import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Services.ProfessorService;
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
class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProfessorController(professorService))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    @Test
    void criarDeveRetornar201() throws Exception {
        ProfessorDTO resposta = professorDTO(1L, "987.654.321-00", "Bruno", "Lima", "Matemática");
        when(professorService.criar(any(ProfessorDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/professores")
                        .contentType("application/json")
                        .content("""
                                {"nome":"Bruno","sobrenome":"Lima","cpf":"987.654.321-00","dataNascimento":"1980-05-20","cidade":"Recife","estado":"PE","paisOrigem":"Brasil","telefone":"81999999999","email":"bruno@email.com","senha":"senhaSegura1","especialidade":"Matemática"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L))
                .andExpect(jsonPath("$.especialidade").value("Matemática"));
    }

    @Test
    void criarComImagemDeveRetornar201() throws Exception {
        ProfessorDTO resposta = professorDTO(1L, "987.654.321-00", "Bruno", "Lima", "Matemática");
        when(professorService.criar(any(ProfessorDTO.class), any())).thenReturn(resposta);

        MockMultipartFile dados = new MockMultipartFile("dados", "", "application/json", """
                {"nome":"Bruno","sobrenome":"Lima","cpf":"987.654.321-00","dataNascimento":"1980-05-20","cidade":"Recife","estado":"PE","paisOrigem":"Brasil","telefone":"81999999999","email":"bruno@email.com","senha":"senhaSegura1","especialidade":"Matemática"}
                """.getBytes());
        MockMultipartFile imagem = new MockMultipartFile("imagemPerfil", "foto.png", "image/png", new byte[] { 1, 2, 3 });

        mockMvc.perform(multipart("/api/professores").file(dados).file(imagem))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_usuario").value(1L));
    }

    @Test
    void listarTodosDeveRetornar200() throws Exception {
        when(professorService.listarTodos()).thenReturn(List.of(professorDTO(1L, "987.654.321-00", "Bruno", "Lima", "Matemática")));

        mockMvc.perform(get("/api/professores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especialidade").value("Matemática"));
    }

    @Test
    void buscarPorIdDeveRetornar200() throws Exception {
        when(professorService.buscarPorId(1L)).thenReturn(professorDTO(1L, "987.654.321-00", "Bruno", "Lima", "Matemática"));

        mockMvc.perform(get("/api/professores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(1L));

        verify(professorService).buscarPorId(eq(1L));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        ProfessorController controller = new ProfessorController(professorService);
        ProfessorDTO request = professorDTO(1L, "987.654.321-01", "Bruno Lima", "Lima", "Estatística");
        when(professorService.atualizar(eq(1L), any(ProfessorDTO.class))).thenReturn(request);

        ResponseEntity<ProfessorDTO> response = controller.atualizar(1L, request);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals("Estatística", response.getBody().getEspecialidade());
    }

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/professores/1"))
                .andExpect(status().isNoContent());

        verify(professorService).deletar(1L);
    }

    @Test
    void criarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/professores")
                        .contentType("application/json")
                        .content("""
                                {"nome":"","sobrenome":"","cpf":"","dataNascimento":null,"cidade":"","estado":"","paisOrigem":"","telefone":"","email":"","senha":"","especialidade":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    private ProfessorDTO professorDTO(Long id, String cpf, String nome, String sobrenome, String especialidade) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId_usuario(id);
        dto.setNome(nome);
        dto.setSobrenome(sobrenome);
        dto.setCpf(cpf);
        dto.setDataNascimento(LocalDate.of(1980, 5, 20));
        dto.setCidade("Recife");
        dto.setEstado("PE");
        dto.setPaisOrigem("Brasil");
        dto.setTelefone("81999999999");
        dto.setEmail("bruno@email.com");
        dto.setSenha("senhaSegura1");
        dto.setEspecialidade(especialidade);
        return dto;
    }
}