package org.Pratica_SpringBoot.Controllers;
import org.Pratica_SpringBoot.Models.DTOs.DisciplinaDTO;
import org.Pratica_SpringBoot.Services.DisciplinaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> criar(@Valid @RequestBody DisciplinaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DisciplinaDTO>> listarTodos(
            @ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(disciplinaService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(disciplinaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody DisciplinaDTO dto) {
        return ResponseEntity.ok(disciplinaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        disciplinaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}