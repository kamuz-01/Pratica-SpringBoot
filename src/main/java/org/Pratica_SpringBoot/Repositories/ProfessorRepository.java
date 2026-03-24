package org.Pratica_SpringBoot.Repositories;

import java.util.Optional;

import org.Pratica_SpringBoot.Models.Entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByCpf(String cpf);

    Optional<Professor> findByEmail(String email);
}