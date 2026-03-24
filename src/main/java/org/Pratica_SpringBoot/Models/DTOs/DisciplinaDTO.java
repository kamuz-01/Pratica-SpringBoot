package org.Pratica_SpringBoot.Models.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {

    private Long id_disciplina;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Size(min = 3, max = 100, message = "O nome da disciplina deve ter entre 3 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "O código da disciplina é obrigatório")
    @Size(max = 20, message = "O código da disciplina deve ter no máximo 20 caracteres")
    private String codigo;

    @NotNull(message = "A carga horária é obrigatória")
    @Min(value = 20, message = "A carga horária mínima é 20 horas")
    @Max(value = 400, message = "A carga horária máxima é 400 horas")
    private Integer cargaHoraria;

    @NotNull(message = "O curso é obrigatório")
    private Long id_curso;

    @NotNull(message = "O professor é obrigatório")
    private Long id_professor;
}