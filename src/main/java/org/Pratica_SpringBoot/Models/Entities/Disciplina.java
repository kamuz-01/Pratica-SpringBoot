package org.Pratica_SpringBoot.Models.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_disciplina;

    public Long getId() {
        return id_disciplina;
    }

    public void setId(Long id) {
        this.id_disciplina = id;
    }

    @Column(name = "nome_disciplina", nullable = false, length = 100)
    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Size(min = 3, max = 100, message = "O nome da disciplina deve ter entre 3 e 100 caracteres")
    private String nomeDisciplina;

    @Column(name = "descricao_disciplina", length = 500)
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricaoDisciplina;

    @Column(name = "codigo_disciplina", nullable = false, length = 20)
    @NotBlank(message = "O código da disciplina é obrigatório")
    @Size(max = 20, message = "O código da disciplina deve ter no máximo 20 caracteres")
    private String codigoDisciplina;

    @Column(name = "carga_horaria", nullable = false)
    @NotNull(message = "A carga horária é obrigatória")
    @Min(value = 20, message = "A carga horária mínima é 20 horas")
    @Max(value = 400, message = "A carga horária máxima é 400 horas")
    private Integer cargaHoraria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_professor", nullable = false)
    @NotNull(message = "A disciplina deve ter um professor")
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    @NotNull(message = "A disciplina deve estar associada a um curso")
    private Curso curso;

    @OneToMany(mappedBy = "disciplina")
    private List<Matricula> matriculas = new ArrayList<>();
}