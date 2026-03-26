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

import java.time.LocalDate;
import java.util.List;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais;
import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Models.Enums.StatusMatricula;
import org.Pratica_SpringBoot.Services.MatriculaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MatriculaControllerTest {

    @Mock
    private MatriculaService matriculaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MatriculaController(matriculaService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(new SortHandlerMethodArgumentResolver()))
                .setControllerAdvice(new ManipuladorExcecoesGlobais())
                .build();
    }

    @Test
    void criarDeveRetornar201() throws Exception {
        MatriculaDTO resposta = matriculaDTO(1L, 2L, 1, LocalDate.of(2026, 1, 15), 90.0, 8.5, StatusMatricula.ATIVA);
        resposta.setId_matricula(10L);
        when(matriculaService.criar(any(MatriculaDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/matriculas")
                        .contentType("application/json")
                        .content("""
                                {"idEstudante":1,"idDisciplina":2,"semestre":1,"dataMatricula":"2026-01-15","frequencia":90.0,"notaFinal":8.5,"status":"ATIVA"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_matricula").value(10L))
                .andExpect(jsonPath("$.status").value("ATIVA"));
    }

    @Test
    void listarTodosDeveRetornar200() throws Exception {
        when(matriculaService.listarTodos(any(Pageable.class))).thenReturn(new PageImpl<>(
                List.of(matriculaDTO(1L, 2L, 1, LocalDate.of(2026, 1, 15), 90.0, 8.5, StatusMatricula.ATIVA)), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/matriculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idEstudante").value(1L));
    }

    @Test
    void buscarPorIdDeveRetornar200() throws Exception {
        MatriculaDTO resposta = matriculaDTO(1L, 2L, 1, LocalDate.of(2026, 1, 15), 90.0, 8.5, StatusMatricula.ATIVA);
        resposta.setId_matricula(10L);
        when(matriculaService.buscarPorId(10L)).thenReturn(resposta);

        mockMvc.perform(get("/api/matriculas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_matricula").value(10L));

        verify(matriculaService).buscarPorId(eq(10L));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        MatriculaDTO resposta = matriculaDTO(1L, 2L, 2, LocalDate.of(2026, 2, 15), 95.0, 9.0, StatusMatricula.CONCLUIDA);
        resposta.setId_matricula(10L);
        when(matriculaService.atualizar(eq(10L), any(MatriculaDTO.class))).thenReturn(resposta);

        mockMvc.perform(put("/api/matriculas/10")
                        .contentType("application/json")
                        .content("""
                                {"idEstudante":1,"idDisciplina":2,"semestre":2,"dataMatricula":"2026-02-15","frequencia":95.0,"notaFinal":9.0,"status":"CONCLUIDA"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semestre").value(2));
    }

    @Test
    void deletarDeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/matriculas/10"))
                .andExpect(status().isNoContent());

        verify(matriculaService).deletar(10L);
    }

    @Test
    void criarComPayloadInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType("application/json")
                        .content("""
                                {"idEstudante":null,"idDisciplina":null,"semestre":0,"frequencia":101.0,"status":null}
                                """))
                .andExpect(status().isBadRequest());
    }

    private MatriculaDTO matriculaDTO(Long idEstudante, Long idDisciplina, Integer semestre, LocalDate dataMatricula,
            Double frequencia, Double notaFinal, StatusMatricula status) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setIdEstudante(idEstudante);
        dto.setIdDisciplina(idDisciplina);
        dto.setSemestre(semestre);
        dto.setDataMatricula(dataMatricula);
        dto.setFrequencia(frequencia);
        dto.setNotaFinal(notaFinal);
        dto.setStatus(status);
        return dto;
    }
}