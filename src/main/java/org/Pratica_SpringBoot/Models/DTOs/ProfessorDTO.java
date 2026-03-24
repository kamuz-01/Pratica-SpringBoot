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
public class ProfessorDTO extends UsuarioDTO {

    @NotBlank(message = "A especialidade é obrigatória")
    @Size(min = 3, max = 80, message = "A especialidade deve ter entre 3 e 80 caracteres")
    private String especialidade;
}