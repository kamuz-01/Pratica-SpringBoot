package org.Pratica_SpringBoot.Controllers;

import org.Pratica_SpringBoot.Models.DTOs.MatriculaDTO;
import org.Pratica_SpringBoot.Services.MatriculaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> criar(@Valid @RequestBody MatriculaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<MatriculaDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(matriculaService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MatriculaDTO dto) {
        return ResponseEntity.ok(matriculaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        matriculaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}