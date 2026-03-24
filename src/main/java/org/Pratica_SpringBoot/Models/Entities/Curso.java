package org.Pratica_SpringBoot.Models.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "curso")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCurso;

	public Long getId() {
		return idCurso;
	}

	public void setId(Long id) {
		this.idCurso = id;
	}

	@Column(name = "codigo_curso", nullable = false, unique = true, length = 20)
	@NotBlank(message = "O código do curso é obrigatório")
	@Size(max = 20, message = "O código do curso deve ter no máximo 20 caracteres")
	private String codigoCurso;

	@Column(name = "nome_curso", nullable = false, length = 100)
	@NotBlank(message = "O nome do curso é obrigatório")
	@Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres")
	private String nomeCurso;

	@Column(name = "descricao_curso", length = 500)
	@Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
	private String descricaoCurso;

	@OneToMany(mappedBy = "curso")
	private List<Disciplina> disciplinas = new ArrayList<>();
}