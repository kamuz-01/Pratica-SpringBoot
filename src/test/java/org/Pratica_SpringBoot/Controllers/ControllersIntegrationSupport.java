package org.Pratica_SpringBoot.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.CursoDTO;
import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Services.CursoService;
import org.Pratica_SpringBoot.Services.DisciplinaService;
import org.Pratica_SpringBoot.Services.EstudanteService;
import org.Pratica_SpringBoot.Services.MatriculaService;
import org.Pratica_SpringBoot.Services.ProfessorService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:pratica_springboot_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "spring.jpa.show-sql=false",
                "spring.jpa.open-in-view=false"
        })
@AutoConfigureMockMvc
    @Import(ControllersIntegrationSupport.TestBeans.class)
public abstract class ControllersIntegrationSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected CursoService cursoService;

    @Autowired
    protected DisciplinaService disciplinaService;

    @Autowired
    protected EstudanteService estudanteService;

    @Autowired
    protected MatriculaService matriculaService;

    @Autowired
    protected ProfessorService professorService;

    protected void stubCursoListarPadrao() {
        when(cursoService.listarTodos()).thenReturn(java.util.List.of(cursoDTO(1L, "ADS", "Análise e Desenvolvimento de Sistemas", "Curso de tecnologia")));
    }

    protected void stubCursoNaoEncontrado() {
        when(cursoService.buscarPorId(99L)).thenThrow(new java.util.NoSuchElementException("Curso não encontrado: 99"));
    }

    protected void stubDisciplinaListarVazio() {
        when(disciplinaService.listarTodos()).thenReturn(java.util.List.of());
    }

    protected void stubEstudanteCriarImagem() {
        when(estudanteService.criar(any(EstudanteDTO.class), any())).thenReturn(estudanteDTO(1L, "123.456.789-09", "Ana", "Silva", "20240001"));
    }

    protected void stubEstudanteCpfDuplicado() {
        when(estudanteService.criar(any(EstudanteDTO.class))).thenThrow(new CpfDuplicadoException("Já existe um estudante cadastrado com este CPF"));
    }

    protected void stubProfessorCriarImagem() {
        when(professorService.criar(any(ProfessorDTO.class), any())).thenReturn(professorDTO(1L, "987.654.321-00", "Bruno", "Lima", "Matemática"));
    }

    protected void stubMatriculaCriar() {
        when(matriculaService.criar(any())).thenReturn(new org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO());
    }

    @TestConfiguration
    @SuppressWarnings("unused")
    static class TestBeans {

        @Bean
        @Primary
        CursoService cursoService() {
            return Mockito.mock(CursoService.class);
        }

        @Bean
        @Primary
        DisciplinaService disciplinaService() {
            return Mockito.mock(DisciplinaService.class);
        }

        @Bean
        @Primary
        EstudanteService estudanteService() {
            return Mockito.mock(EstudanteService.class);
        }

        @Bean
        @Primary
        MatriculaService matriculaService() {
            return Mockito.mock(MatriculaService.class);
        }

        @Bean
        @Primary
        ProfessorService professorService() {
            return Mockito.mock(ProfessorService.class);
        }
    }

    protected CursoDTO cursoDTO(Long id, String codigo, String nome, String descricao) {
        CursoDTO dto = new CursoDTO();
        dto.setId_curso(id);
        dto.setCodigo(codigo);
        dto.setNome(nome);
        dto.setDescricao(descricao);
        return dto;
    }

    protected EstudanteDTO estudanteDTO(Long id, String cpf, String nome, String sobrenome, String matricula) {
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

    protected ProfessorDTO professorDTO(Long id, String cpf, String nome, String sobrenome, String especialidade) {
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

    protected byte[] estudanteDadosBytes() {
        return """
                {"nome":"Ana","sobrenome":"Silva","cpf":"123.456.789-09","dataNascimento":"2000-01-01","cidade":"São Paulo","estado":"SP","paisOrigem":"Brasil","telefone":"11999999999","email":"ana@email.com","senha":"senhaSegura1","matricula":"20240001"}
                """.getBytes();
    }

    protected byte[] professorDadosBytes() {
        return """
                {"nome":"Bruno","sobrenome":"Lima","cpf":"987.654.321-00","dataNascimento":"1980-05-20","cidade":"Recife","estado":"PE","paisOrigem":"Brasil","telefone":"81999999999","email":"bruno@email.com","senha":"senhaSegura1","especialidade":"Matemática"}
                """.getBytes();
    }

    protected MockMultipartFile estudanteDadosMultipart() {
        return new MockMultipartFile("dados", "", "application/json", estudanteDadosBytes());
    }

    protected MockMultipartFile professorDadosMultipart() {
        return new MockMultipartFile("dados", "", "application/json", professorDadosBytes());
    }
}