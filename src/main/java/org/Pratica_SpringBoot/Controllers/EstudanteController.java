package org.Pratica_SpringBoot.Controllers;

import org.Pratica_SpringBoot.Models.DTOs.EstudanteDTO;
import org.Pratica_SpringBoot.Services.EstudanteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    @PostMapping
    public ResponseEntity<EstudanteDTO> criar(@Valid @RequestBody EstudanteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteService.criar(dto));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EstudanteDTO> criarComImagem(@Valid @RequestPart("dados") EstudanteDTO dto,
            @RequestPart(value = "imagemPerfil", required = false) MultipartFile imagemPerfil) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteService.criar(dto, imagemPerfil));
    }

    @GetMapping
    public ResponseEntity<Page<EstudanteDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(estudanteService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudanteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estudanteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudanteDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EstudanteDTO dto) {
        return ResponseEntity.ok(estudanteService.atualizar(id, dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EstudanteDTO> atualizarComImagem(@PathVariable Long id,
            @Valid @RequestPart("dados") EstudanteDTO dto,
            @RequestPart(value = "imagemPerfil", required = false) MultipartFile imagemPerfil) {
        return ResponseEntity.ok(estudanteService.atualizar(id, dto, imagemPerfil));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estudanteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}