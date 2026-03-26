package org.Pratica_SpringBoot.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Models.Mappers.EstudanteMapper;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
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
class EstudanteServiceTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private SenhaCriptografiaService senhaCriptografiaService;

    @Mock
    private UsuarioImagemStorageService usuarioImagemStorageService;

    private EstudanteService estudanteService;

    @BeforeEach
    void setUp() {
        estudanteService = new EstudanteService(estudanteRepository, Mappers.getMapper(EstudanteMapper.class),
                senhaCriptografiaService, usuarioImagemStorageService);
    }

    @Test
    void criarDeveSalvarEstudanteEImagemQuandoServicoRetornaCaminho() {
        EstudanteDTO estudanteDTO = estudanteDTO("123.456.789-09", "Ana", "Silva", "senhaSegura1", "20240001");
        when(estudanteRepository.findByCpf("123.456.789-09")).thenReturn(Optional.empty());
        when(senhaCriptografiaService.criptografar("senhaSegura1")).thenReturn("hash-inicial");
        when(estudanteRepository.save(argThat((Estudante estudante) -> true))).thenAnswer(invocation -> {
            Estudante estudante = invocation.getArgument(0);
            if (estudante.getId() == null) {
                estudante.setId(1L);
            }
            return estudante;
        });
        when(usuarioImagemStorageService.salvarImagemPerfil(1L, null)).thenReturn("imagens-usuarios/1/foto-perfil.jpg");

        EstudanteDTO resultado = estudanteService.criar(estudanteDTO);

        assertEquals(1L, resultado.getId_usuario());
        assertEquals("imagens-usuarios/1/foto-perfil.jpg", resultado.getImagemPerfil());
        assertEquals("hash-inicial", resultado.getSenha());
    }

    @Test
    void criarDeveLancarQuandoCpfJaExiste() {
        EstudanteDTO estudanteDTO = estudanteDTO("123.456.789-09", "Ana", "Silva", "senhaSegura1", "20240001");
        when(estudanteRepository.findByCpf("123.456.789-09")).thenReturn(Optional.of(estudante(2L, "123.456.789-09", "hash", null)));

        CpfDuplicadoException exception = assertThrows(CpfDuplicadoException.class, () -> estudanteService.criar(estudanteDTO));
        assertEquals("Já existe um estudante cadastrado com este CPF", exception.getMessage());
    }

    @Test
    void buscarPorIdDeveRetornarDTO() {
        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(estudante(1L, "123.456.789-09", "hash", null)));

        EstudanteDTO resultado = estudanteService.buscarPorId(1L);

        assertEquals(1L, resultado.getId_usuario());
        assertEquals("123.456.789-09", resultado.getCpf());
    }

    @Test
    void atualizarDeveSalvarNovosDadosEManterImagemQuandoNaoHaNovaFoto() {
        Estudante existente = estudante(1L, "123.456.789-09", "hash-antigo", "foto-antiga");
        EstudanteDTO novoDTO = estudanteDTO("123.456.789-10", "Ana Maria", "Silva", "senhaNova1", "20240002");
        novoDTO.setCidade("Campinas");
        novoDTO.setTelefone("11988888888");
        novoDTO.setEmail("ana.maria@email.com");

        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estudanteRepository.findByCpf("123.456.789-10")).thenReturn(Optional.empty());
        when(senhaCriptografiaService.criptografar("senhaNova1")).thenReturn("hash-novo");
        when(usuarioImagemStorageService.salvarImagemPerfil(1L, null)).thenReturn(null);
        when(estudanteRepository.save(argThat((Estudante estudante) -> true))).thenAnswer(invocation -> invocation.getArgument(0));

        EstudanteDTO resultado = estudanteService.atualizar(1L, novoDTO);

        assertEquals("Ana Maria", resultado.getNome());
        assertEquals("123.456.789-10", resultado.getCpf());
        assertEquals("hash-novo", resultado.getSenha());
        assertEquals("foto-antiga", resultado.getImagemPerfil());
    }

    @Test
    void atualizarDeveLancarQuandoCpfJaExisteEmOutroRegistro() {
        Estudante existente = estudante(1L, "123.456.789-09", "hash", null);
        EstudanteDTO novoDTO = estudanteDTO("123.456.789-10", "Ana Maria", "Silva", "senhaNova1", "20240002");
        novoDTO.setCidade("Campinas");
        novoDTO.setTelefone("11988888888");
        novoDTO.setEmail("ana.maria@email.com");

        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estudanteRepository.findByCpf("123.456.789-10")).thenReturn(Optional.of(estudante(2L, "123.456.789-10", "hash", null)));

        CpfDuplicadoException exception = assertThrows(CpfDuplicadoException.class, () -> estudanteService.atualizar(1L, novoDTO));
        assertEquals("Já existe um estudante cadastrado com este CPF", exception.getMessage());
    }

    @Test
    void listarTodosDeveConverterEntidades() {
        when(estudanteRepository.findAll(org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class))).thenReturn(
            new org.springframework.data.domain.PageImpl<>(List.of(estudante(1L, "123.456.789-09", "hash", null)),
                org.springframework.data.domain.PageRequest.of(0, 10), 1));

        org.springframework.data.domain.Page<EstudanteDTO> resultado = estudanteService.listarTodos(
            org.springframework.data.domain.PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).getId_usuario());
    }

    @Test
    void deletarDeveRemoverEstudanteExistente() {
        Estudante existente = estudante(1L, "123.456.789-09", "hash", null);
        when(estudanteRepository.findById(1L)).thenReturn(Optional.of(existente));

        estudanteService.deletar(1L);

        ArgumentCaptor<Estudante> captor = ArgumentCaptor.forClass(Estudante.class);
        verify(estudanteRepository).delete(captor.capture());
        assertEquals(existente, captor.getValue());
    }

    private Estudante estudante(Long id, String cpf, String senha, String imagemPerfil) {
        Estudante estudante = new Estudante();
        estudante.setId(id);
        estudante.setNome("Ana");
        estudante.setSobrenome("Silva");
        estudante.setCpf(cpf);
        estudante.setDataNascimento(LocalDate.of(2000, 1, 1));
        estudante.setCidade("São Paulo");
        estudante.setEstado("SP");
        estudante.setPaisOrigem("Brasil");
        estudante.setTelefone("11999999999");
        estudante.setEmail("ana@email.com");
        estudante.setSenha(senha);
        estudante.setImagemPerfil(imagemPerfil);
        estudante.setMatricula("20240001");
        return estudante;
    }

    private EstudanteDTO estudanteDTO(String cpf, String nome, String sobrenome, String senha, String matricula) {
        EstudanteDTO dto = new EstudanteDTO();
        dto.setNome(nome);
        dto.setSobrenome(sobrenome);
        dto.setCpf(cpf);
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));
        dto.setCidade("São Paulo");
        dto.setEstado("SP");
        dto.setPaisOrigem("Brasil");
        dto.setTelefone("11999999999");
        dto.setEmail("ana@email.com");
        dto.setSenha(senha);
        dto.setMatricula(matricula);
        return dto;
    }
}