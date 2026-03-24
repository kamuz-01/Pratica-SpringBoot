package org.Pratica_SpringBoot.Models.Entities;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "professor")
public class Professor extends Usuario {

    @Column(nullable = false, length = 80)
    @NotBlank(message = "A especialidade é obrigatória")
    @Size(min = 3, max = 80, message = "A especialidade deve ter entre 3 e 80 caracteres")
    private String especialidade;

    @OneToMany(mappedBy = "professor")
    private List<Disciplina> disciplinas = new ArrayList<>();
}