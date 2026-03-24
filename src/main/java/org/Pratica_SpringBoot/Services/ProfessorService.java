package org.Pratica_SpringBoot.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final SenhaCriptografiaService senhaCriptografiaService;
    private final UsuarioImagemStorageService usuarioImagemStorageService;

    public ProfessorService(ProfessorRepository professorRepository, SenhaCriptografiaService senhaCriptografiaService,
            UsuarioImagemStorageService usuarioImagemStorageService) {
        this.professorRepository = professorRepository;
        this.senhaCriptografiaService = senhaCriptografiaService;
        this.usuarioImagemStorageService = usuarioImagemStorageService;
    }

    public ProfessorDTO criar(ProfessorDTO dto) {
        return criar(dto, null);
    }

    public ProfessorDTO criar(ProfessorDTO dto, MultipartFile imagemPerfil) {
        validarCpfDuplicado(dto.getCpf(), null);
        Professor professor = toEntity(dto);
        professor.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));
        professor.setImagemPerfil(null);

        Professor salvo = professorRepository.save(professor);
        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(salvo.getId(), imagemPerfil);

        if (caminhoImagem != null) {
            salvo.setImagemPerfil(caminhoImagem);
            salvo = professorRepository.save(salvo);
        }

        return toDTO(salvo);
    }

    public List<ProfessorDTO> listarTodos() {
        return professorRepository.findAll().stream().map(this::toDTO).toList();
    }

    public ProfessorDTO buscarPorId(Long id) {
        return toDTO(buscarEntidadePorId(id));
    }

    public ProfessorDTO atualizar(Long id, ProfessorDTO dto) {
        return atualizar(id, dto, null);
    }

    public ProfessorDTO atualizar(Long id, ProfessorDTO dto, MultipartFile imagemPerfil) {
        Professor professor = buscarEntidadePorId(id);
        validarCpfDuplicado(dto.getCpf(), id);
        atualizarEntidade(professor, dto);
        professor.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));

        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(professor.getId(), imagemPerfil);
        if (caminhoImagem != null) {
            professor.setImagemPerfil(caminhoImagem);
        }

        return toDTO(professorRepository.save(professor));
    }

    public void deletar(Long id) {
        professorRepository.delete(buscarEntidadePorId(id));
    }

    private Professor buscarEntidadePorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Professor não encontrado: " + id));
    }

    private Professor toEntity(ProfessorDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO de professor não pode ser nulo");
        }

        Professor professor = new Professor();
        atualizarEntidade(professor, dto);
        return professor;
    }

    private void atualizarEntidade(Professor professor, ProfessorDTO dto) {
        professor.setId(dto.getId_usuario());
        professor.setNome(dto.getNome());
        professor.setSobrenome(dto.getSobrenome());
        professor.setCpf(dto.getCpf());
        professor.setDataNascimento(dto.getDataNascimento());
        professor.setCidade(dto.getCidade());
        professor.setEstado(dto.getEstado());
        professor.setPaisOrigem(dto.getPaisOrigem());
        professor.setTelefone(dto.getTelefone());
        professor.setEmail(dto.getEmail());
        professor.setEspecialidade(dto.getEspecialidade());
    }

    private void validarCpfDuplicado(String cpf, Long idAtual) {
        professorRepository.findByCpf(cpf)
                .filter(professor -> idAtual == null || !professor.getId().equals(idAtual))
                .ifPresent(professor -> {
                    throw new CpfDuplicadoException("Já existe um professor cadastrado com este CPF");
                });
    }

    private ProfessorDTO toDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId_usuario(professor.getId());
        dto.setNome(professor.getNome());
        dto.setSobrenome(professor.getSobrenome());
        dto.setCpf(professor.getCpf());
        dto.setDataNascimento(professor.getDataNascimento());
        dto.setCidade(professor.getCidade());
        dto.setEstado(professor.getEstado());
        dto.setPaisOrigem(professor.getPaisOrigem());
        dto.setTelefone(professor.getTelefone());
        dto.setEmail(professor.getEmail());
        dto.setSenha(professor.getSenha());
        dto.setImagemPerfil(professor.getImagemPerfil());
        dto.setEspecialidade(professor.getEspecialidade());
        return dto;
    }
}