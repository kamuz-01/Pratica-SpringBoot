package org.Pratica_SpringBoot.Models.Entities;

import java.time.LocalDate;

import org.Pratica_SpringBoot.Models.Enums.StatusMatricula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Entity
@Table(name = "matricula", uniqueConstraints = @UniqueConstraint(columnNames = {"id_estudante", "id_disciplina", "semestre"}))
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_matricula;

    public Long getId() {
        return id_matricula;
    }

    public void setId(Long id) {
        this.id_matricula = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estudante", nullable = false)
    @NotNull(message = "O estudante é obrigatório")
    private Estudante estudante;

    @Column(nullable = false)
    @NotNull(message = "O semestre é obrigatório")
    @Min(value = 1, message = "O semestre deve ser no mínimo 1")
    private Integer semestre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_disciplina", nullable = false)
    @NotNull(message = "A disciplina é obrigatória")
    private Disciplina disciplina;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula = LocalDate.now();

    @Column(nullable = false)
    @NotNull(message = "A frequência é obrigatória")
    @Min(0)
    @Max(100)
    private Double frequencia;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    @Column(name = "nota_final", nullable = false)
    private Double notaFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "O status é obrigatório")
    private StatusMatricula status;
}