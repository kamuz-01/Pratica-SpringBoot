package org.Pratica_SpringBoot.Controllers;

import java.util.List;

import org.Pratica_SpringBoot.Models.DTOs.ProfessorDTO;
import org.Pratica_SpringBoot.Services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> criar(@Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.criar(dto));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfessorDTO> criarComImagem(@Valid @RequestPart("dados") ProfessorDTO dto,
            @RequestPart(value = "imagemPerfil", required = false) MultipartFile imagemPerfil) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.criar(dto, imagemPerfil));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodos() {
        return ResponseEntity.ok(professorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(professorService.atualizar(id, dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfessorDTO> atualizarComImagem(@PathVariable Long id,
            @Valid @RequestPart("dados") ProfessorDTO dto,
            @RequestPart(value = "imagemPerfil", required = false) MultipartFile imagemPerfil) {
        return ResponseEntity.ok(professorService.atualizar(id, dto, imagemPerfil));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}