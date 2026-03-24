package org.Pratica_SpringBoot.Models.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {

    private Long id_curso;

    @NotBlank(message = "O código do curso é obrigatório")
    @Size(max = 20, message = "O código do curso deve ter no máximo 20 caracteres")
    private String codigo;

    @NotBlank(message = "O nome do curso é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
}