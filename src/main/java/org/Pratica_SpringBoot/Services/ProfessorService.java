package org.Pratica_SpringBoot.Services;

import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais.CpfDuplicadoException;
import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.Pratica_SpringBoot.Models.Mappers.ProfessorMapper;
import org.Pratica_SpringBoot.Repositories.DisciplinaRepository;
import org.Pratica_SpringBoot.Repositories.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;
    private final SenhaCriptografiaService senhaCriptografiaService;
    private final UsuarioImagemStorageService usuarioImagemStorageService;
    private final DisciplinaRepository disciplinaRepository;

    public ProfessorService(ProfessorRepository professorRepository, ProfessorMapper professorMapper,
            SenhaCriptografiaService senhaCriptografiaService,
            UsuarioImagemStorageService usuarioImagemStorageService,
            DisciplinaRepository disciplinaRepository) {
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
        this.senhaCriptografiaService = senhaCriptografiaService;
        this.usuarioImagemStorageService = usuarioImagemStorageService;
        this.disciplinaRepository = disciplinaRepository;
    }

    public ProfessorDTO criar(ProfessorDTO dto) {
        return criar(dto, null);
    }

    public ProfessorDTO criar(ProfessorDTO dto, MultipartFile imagemPerfil) {
        validarCpfDuplicado(dto.getCpf(), null);
        Professor professor = professorMapper.toEntity(dto);
        professor.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));
        professor.setImagemPerfil(null);

        Professor salvo = professorRepository.save(professor);
        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(salvo.getId(), imagemPerfil);

        if (caminhoImagem != null) {
            salvo.setImagemPerfil(caminhoImagem);
            salvo = professorRepository.save(salvo);
        }

        return professorMapper.toDto(salvo);
    }

    public Page<ProfessorDTO> listarTodos(Pageable pageable) {
        return professorRepository.findAll(pageable).map(professorMapper::toDto);
    }

    public ProfessorDTO buscarPorId(Long id) {
        return professorMapper.toDto(buscarEntidadePorId(id));
    }

    public ProfessorDTO atualizar(Long id, ProfessorDTO dto) {
        return atualizar(id, dto, null);
    }

    public ProfessorDTO atualizar(Long id, ProfessorDTO dto, MultipartFile imagemPerfil) {
        Professor professor = buscarEntidadePorId(id);
        validarCpfDuplicado(dto.getCpf(), id);
        professorMapper.updateEntityFromDto(dto, professor);
        professor.setSenha(senhaCriptografiaService.criptografar(dto.getSenha()));

        String caminhoImagem = usuarioImagemStorageService.salvarImagemPerfil(id, imagemPerfil);
        if (caminhoImagem != null) {
            professor.setImagemPerfil(caminhoImagem);
        }

        return professorMapper.toDto(professorRepository.save(professor));
    }

    public void deletar(Long id) {
        Professor professor = buscarEntidadePorId(id);
        if (disciplinaRepository.existsByProfessor_Id(id)) {
            throw new IllegalStateException("Este professor não pode ser excluído pois possui disciplinas vinculadas.");
        }

        professorRepository.delete(professor);
    }

    private Professor buscarEntidadePorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Professor não encontrado: " + id));
    }

    private void validarCpfDuplicado(String cpf, Long idAtual) {
        professorRepository.findByCpf(cpf)
                .filter(professor -> idAtual == null || !professor.getId().equals(idAtual))
                .ifPresent(professor -> {
                    throw new CpfDuplicadoException("Já existe um professor cadastrado com este CPF");
                });
    }
}
