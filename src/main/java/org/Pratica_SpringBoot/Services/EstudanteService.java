package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.Pratica_SpringBoot.Models.Mappers.EstudanteMapper;
import org.Pratica_SpringBoot.Repositories.EstudanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final EstudanteMapper estudanteMapper;
    private final SenhaCriptografiaService senhaCriptografiaService;
    private final UsuarioImagemStorageService usuarioImagemStorageService;

    public EstudanteService(EstudanteRepository estudanteRepository, EstudanteMapper estudanteMapper,
            SenhaCriptografiaService senhaCriptografiaService,
            UsuarioImagemStorageService usuarioImagemStorageService) {
        this.estudanteRepository = estudanteRepository;
        this.estudanteMapper = estudanteMapper;
        this.senhaCriptografiaService = senhaCriptografiaService;
        this.usuarioImagemStorageService = usuarioImagemStorageService;
    }

    public EstudanteDTO criar(EstudanteDTO dto) {
        return criar(dto, null);
    }

    public EstudanteDTO criar(EstudanteDTO dto, MultipartFile imagemPerfil) {
        validarCpfDuplicado(dto.getCpf(), null);
        Estudante estudante = estudanteMapper.toEntity(dto);
        estudante.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));
        estudante.setImagemPerfil(null);

        Estudante salvo = estudanteRepository.save(Objects.requireNonNull(estudante));
        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(salvo.getId(), imagemPerfil);

        if (caminhoImagem != null) {
            salvo.setImagemPerfil(caminhoImagem);
            salvo = estudanteRepository.save(salvo);
        }

        return estudanteMapper.toDto(salvo);
    }

    public List<EstudanteDTO> listarTodos() {
        return estudanteRepository.findAll().stream().map(estudanteMapper::toDto).toList();
    }

    public EstudanteDTO buscarPorId(Long id) {
        return estudanteMapper.toDto(buscarEntidadePorId(id));
    }

    public EstudanteDTO atualizar(Long id, EstudanteDTO dto) {
        return atualizar(id, dto, null);
    }

    public EstudanteDTO atualizar(Long id, EstudanteDTO dto, MultipartFile imagemPerfil) {
        Estudante estudante = buscarEntidadePorId(id);
        validarCpfDuplicado(dto.getCpf(), id);
        estudanteMapper.updateEntityFromDto(dto, estudante);
        estudante.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));

        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(id, imagemPerfil);
        if (caminhoImagem != null) {
            estudante.setImagemPerfil(caminhoImagem);
        }

        return estudanteMapper.toDto(estudanteRepository.save(Objects.requireNonNull(estudante)));
    }

    public void deletar(Long id) {
        Estudante estudante = buscarEntidadePorId(id);
        estudanteRepository.delete(Objects.requireNonNull(estudante));
    }

    private Estudante buscarEntidadePorId(Long id) {
        return estudanteRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("Estudante não encontrado: " + id));
    }

    private void validarCpfDuplicado(String cpf, Long idAtual) {
        estudanteRepository.findByCpf(cpf)
                .filter(estudante -> idAtual == null || !estudante.getId().equals(idAtual))
                .ifPresent(estudante -> {
                    throw new CpfDuplicadoException("Já existe um estudante cadastrado com este CPF");
                });
    }
}
