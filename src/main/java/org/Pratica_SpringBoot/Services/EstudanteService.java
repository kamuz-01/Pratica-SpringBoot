package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final SenhaCriptografiaService senhaCriptografiaService;
    private final UsuarioImagemStorageService usuarioImagemStorageService;

    public EstudanteService(EstudanteRepository estudanteRepository, SenhaCriptografiaService senhaCriptografiaService,
            UsuarioImagemStorageService usuarioImagemStorageService) {
        this.estudanteRepository = estudanteRepository;
        this.senhaCriptografiaService = senhaCriptografiaService;
        this.usuarioImagemStorageService = usuarioImagemStorageService;
    }

    public EstudanteDTO criar(EstudanteDTO dto) {
        return criar(dto, null);
    }

    public EstudanteDTO criar(EstudanteDTO dto, MultipartFile imagemPerfil) {
        validarCpfDuplicado(dto.getCpf(), null);
        Estudante estudante = toEntity(dto);
        estudante.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));
        estudante.setImagemPerfil(null);

        Estudante salvo = estudanteRepository.save(Objects.requireNonNull(estudante));
        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(salvo.getId(), imagemPerfil);

        if (caminhoImagem != null) {
            salvo.setImagemPerfil(caminhoImagem);
            salvo = estudanteRepository.save(salvo);
        }

        return toDTO(salvo);
    }

    public List<EstudanteDTO> listarTodos() {
        return estudanteRepository.findAll().stream().map(this::toDTO).toList();
    }

    public EstudanteDTO buscarPorId(Long id) {
        return toDTO(buscarEntidadePorId(id));
    }

    public EstudanteDTO atualizar(Long id, EstudanteDTO dto) {
        return atualizar(id, dto, null);
    }

    public EstudanteDTO atualizar(Long id, EstudanteDTO dto, MultipartFile imagemPerfil) {
        Estudante estudante = buscarEntidadePorId(id);
        validarCpfDuplicado(dto.getCpf(), id);
        atualizarEntidade(estudante, dto);
        estudante.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));

        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(estudante.getId(), imagemPerfil);
        if (caminhoImagem != null) {
            estudante.setImagemPerfil(caminhoImagem);
        }

        return toDTO(estudanteRepository.save(Objects.requireNonNull(estudante)));
    }

    public void deletar(Long id) {
        Estudante estudante = buscarEntidadePorId(id);
        estudanteRepository.delete(Objects.requireNonNull(estudante));
    }

    private Estudante buscarEntidadePorId(Long id) {
        return estudanteRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("Estudante não encontrado: " + id));
    }

    private Estudante toEntity(EstudanteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de estudante não pode ser nulo");
        }

        Estudante estudante = new Estudante();
        atualizarEntidade(estudante, dto);
        return estudante;
    }

    private void atualizarEntidade(Estudante estudante, EstudanteDTO dto) {
        estudante.setNome(dto.getNome());
        estudante.setSobrenome(dto.getSobrenome());
        estudante.setCpf(dto.getCpf());
        estudante.setDataNascimento(dto.getDataNascimento());
        estudante.setCidade(dto.getCidade());
        estudante.setEstado(dto.getEstado());
        estudante.setPaisOrigem(dto.getPaisOrigem());
        estudante.setTelefone(dto.getTelefone());
        estudante.setEmail(dto.getEmail());
        estudante.setMatricula(dto.getMatricula());
    }

    private void validarCpfDuplicado(String cpf, Long idAtual) {
        estudanteRepository.findByCpf(cpf)
                .filter(estudante -> idAtual == null || !estudante.getId().equals(idAtual))
                .ifPresent(estudante -> {
                    throw new CpfDuplicadoException("Já existe um estudante cadastrado com este CPF");
                });
    }

    private EstudanteDTO toDTO(Estudante estudante) {
        EstudanteDTO dto = new EstudanteDTO();
        dto.setId_usuario(estudante.getId());
        dto.setNome(estudante.getNome());
        dto.setSobrenome(estudante.getSobrenome());
        dto.setCpf(estudante.getCpf());
        dto.setDataNascimento(estudante.getDataNascimento());
        dto.setCidade(estudante.getCidade());
        dto.setEstado(estudante.getEstado());
        dto.setPaisOrigem(estudante.getPaisOrigem());
        dto.setTelefone(estudante.getTelefone());
        dto.setEmail(estudante.getEmail());
        dto.setSenha(estudante.getSenha());
        dto.setImagemPerfil(estudante.getImagemPerfil());
        dto.setMatricula(estudante.getMatricula());
        return dto;
    }
}
