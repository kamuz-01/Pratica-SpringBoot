package org.Pratica_SpringBoot.Models.Entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    
        public Long getId() {
            return id_usuario;
        }
    
        public void setId(Long id) {
            this.id_usuario = id;
        }

    @Column(nullable = false)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "O sobrenome é obrigatório")
    @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres")
    private String sobrenome;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 14, max = 14, message = "O CPF deve ter 11 caracteres")
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "A cidade é obrigatória")
    @Size(min = 2, max = 80, message = "A cidade deve ter entre 2 e 80 caracteres")
    private String cidade;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 80, message = "O estado deve ter entre 2 e 80 caracteres")
    private String estado;

    @Column(name = "pais_origem", nullable = false, length = 80)
    @NotBlank(message = "O país de origem é obrigatório")
    @Size(min = 2, max = 80, message = "O país de origem deve ter entre 2 e 80 caracteres")
    private String paisOrigem;

    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres")
    private String telefone;

    @Column(name = "imagem_perfil", length = 255)
	private String imagemPerfil;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 120, message = "O email deve ter no máximo 120 caracteres")
    private String email;

    @Column(name = "senha", nullable = false, length = 120)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 120, message = "A senha deve ter entre 8 e 120 caracteres")
    private String senha;

    public Usuario(String nome, String sobrenome, String cpf, LocalDate dataNascimento, String cidade, String estado,
            String paisOrigem, String telefone, String imagemPerfil, String email, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.cidade = cidade;
        this.estado = estado;
        this.paisOrigem = paisOrigem;
        this.telefone = telefone;
        this.imagemPerfil = imagemPerfil;
        this.email = email;
        this.senha = senha;
    }
}