package org.Pratica_SpringBoot.Models.DTOs;

import java.time.LocalDate;

import org.Pratica_SpringBoot.Models.Enums.StatusMatricula;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaDTO {

    private Long id_matricula;

    @NotNull(message = "O estudante é obrigatório")
    private Long idEstudante;

    @NotNull(message = "A disciplina é obrigatória")
    private Long idDisciplina;

    @NotNull(message = "O semestre é obrigatório")
    @Min(value = 1, message = "O semestre deve ser no mínimo 1")
    private Integer semestre;

    private LocalDate dataMatricula;

    @NotNull(message = "A frequência é obrigatória")
    @Min(value = 0, message = "A frequência mínima é 0")
    @Max(value = 100, message = "A frequência máxima é 100")
    private Double frequencia;

    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    private Double notaFinal;

    @NotNull(message = "O status é obrigatório")
    private StatusMatricula status;
}