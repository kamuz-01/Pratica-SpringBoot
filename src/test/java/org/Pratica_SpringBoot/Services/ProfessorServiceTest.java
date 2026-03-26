package org.Pratica_SpringBoot.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Models.Mappers.ProfessorMapper;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private SenhaCriptografiaService senhaCriptografiaService;

    @Mock
    private UsuarioImagemStorageService usuarioImagemStorageService;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    private ProfessorService professorService;

    @BeforeEach
    void setUp() {
        professorService = new ProfessorService(professorRepository, Mappers.getMapper(ProfessorMapper.class),
                senhaCriptografiaService, usuarioImagemStorageService, disciplinaRepository);
    }

    @Test
    void criarDeveSalvarProfessor() {
        ProfessorDTO professorDTO = professorDTO("987.654.321-00", "Bruno", "Lima", "senhaSegura1", "Matemática");
        Professor salvo = professor(1L, "987.654.321-00", "hash", null);
        when(professorRepository.findByCpf("987.654.321-00")).thenReturn(Optional.empty());
        when(senhaCriptografiaService.criptografar("senhaSegura1")).thenReturn("hash");
        when(professorRepository.save(argThat((Professor professor) -> true))).thenReturn(salvo);
        when(usuarioImagemStorageService.salvarImagemPerfil(1L, null)).thenReturn(null);

        ProfessorDTO resultado = professorService.criar(professorDTO);

        assertEquals(1L, resultado.getId_usuario());
        assertEquals("Matemática", resultado.getEspecialidade());
        assertEquals("hash", resultado.getSenha());
    }

    @Test
    void criarDeveLancarQuandoCpfDuplicado() {
        ProfessorDTO professorDTO = professorDTO("987.654.321-00", "Bruno", "Lima", "senhaSegura1", "Matemática");
        when(professorRepository.findByCpf("987.654.321-00")).thenReturn(Optional.of(professor(2L, "987.654.321-00", "hash", null)));

        CpfDuplicadoException exception = assertThrows(CpfDuplicadoException.class, () -> professorService.criar(professorDTO));
        assertEquals("Já existe um professor cadastrado com este CPF", exception.getMessage());
    }

    @Test
    void listarTodosDeveConverterEntidades() {
        when(professorRepository.findAll(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(professor(1L, "987.654.321-00", "hash", null)), PageRequest.of(0, 10), 1));

        Page<ProfessorDTO> resultado = professorService.listarTodos(PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).getId_usuario());
    }

    @Test
    void atualizarDevePersistirNovosDados() {
        Professor existente = professor(1L, "987.654.321-00", "hash-antigo", null);
        ProfessorDTO novoDTO = professorDTO("987.654.321-01", "Bruno Lima", "Lima", "senhaNova1", "Estatística");
        novoDTO.setId_usuario(1L);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(professorRepository.findByCpf("987.654.321-01")).thenReturn(Optional.empty());
        when(senhaCriptografiaService.criptografar("senhaNova1")).thenReturn("hash-novo");
        when(professorRepository.save(argThat((Professor professor) -> true))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioImagemStorageService.salvarImagemPerfil(1L, null)).thenReturn(null);

        ProfessorDTO resultado = professorService.atualizar(1L, novoDTO);

        assertEquals("Estatística", resultado.getEspecialidade());
        assertEquals("987.654.321-01", resultado.getCpf());
        assertEquals("hash-novo", resultado.getSenha());
    }

    @Test
    void deletarDeveRemoverProfessor() {
        Professor existente = professor(1L, "987.654.321-00", "hash", null);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(disciplinaRepository.existsByProfessor_Id(1L)).thenReturn(false);

        professorService.deletar(1L);

        ArgumentCaptor<Professor> captor = ArgumentCaptor.forClass(Professor.class);
        verify(professorRepository).delete(captor.capture());
        assertEquals(existente, captor.getValue());
    }

    @Test
    void deletarDeveLancarQuandoProfessorPossuiDisciplinas() {
        Professor existente = professor(1L, "987.654.321-00", "hash", null);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(disciplinaRepository.existsByProfessor_Id(1L)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> professorService.deletar(1L));
        assertEquals("Este professor não pode ser excluído pois possui disciplinas vinculadas.", exception.getMessage());
    }

    private Professor professor(Long id, String cpf, String senha, String imagemPerfil) {
        Professor professor = new Professor();
        professor.setId(id);
        professor.setNome("Bruno");
        professor.setSobrenome("Lima");
        professor.setCpf(cpf);
        professor.setDataNascimento(LocalDate.of(1980, 5, 20));
        professor.setCidade("Recife");
        professor.setEstado("PE");
        professor.setPaisOrigem("Brasil");
        professor.setTelefone("81999999999");
        professor.setEmail("bruno@email.com");
        professor.setSenha(senha);
        professor.setImagemPerfil(imagemPerfil);
        professor.setEspecialidade("Matemática");
        return professor;
    }

    private ProfessorDTO professorDTO(String cpf, String nome, String sobrenome, String senha, String especialidade) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setNome(nome);
        dto.setSobrenome(sobrenome);
        dto.setCpf(cpf);
        dto.setDataNascimento(LocalDate.of(1980, 5, 20));
        dto.setCidade("Recife");
        dto.setEstado("PE");
        dto.setPaisOrigem("Brasil");
        dto.setTelefone("81999999999");
        dto.setEmail("bruno@email.com");
        dto.setSenha(senha);
        dto.setEspecialidade(especialidade);
        return dto;
    }
}