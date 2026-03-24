package org.Pratica_SpringBoot.Models.DTOs;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UsuarioDTO {

	private Long id_usuario;

	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
	private String nome;

	@NotBlank(message = "O sobrenome é obrigatório")
	@Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres")
	private String sobrenome;

	@NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "Formato de CPF inválido")
	private String cpf;

	@NotNull(message = "A data de nascimento é obrigatória")
	@Past(message = "A data de nascimento deve estar no passado")
	private LocalDate dataNascimento;

	@NotBlank(message = "A cidade é obrigatória")
	@Size(min = 2, max = 80, message = "A cidade deve ter entre 2 e 80 caracteres")
	private String cidade;

	@NotBlank(message = "O estado é obrigatório")
	@Size(min = 2, max = 80, message = "O estado deve ter entre 2 e 80 caracteres")
	private String estado;

	@NotBlank(message = "O país de origem é obrigatório")
	@Size(min = 2, max = 80, message = "O país de origem deve ter entre 2 e 80 caracteres")
	private String paisOrigem;

	@NotBlank(message = "O telefone é obrigatório")
	@Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres")
	private String telefone;

	@NotBlank(message = "O email é obrigatório")
	@Email(message = "O email deve ser válido")
	@Size(max = 120, message = "O email deve ter no máximo 120 caracteres")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 8, max = 120, message = "A senha deve ter entre 8 e 120 caracteres")
	private String senha;

	private String imagemPerfil;
}
