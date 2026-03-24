package org.Pratica_SpringBoot.Repositories;

import java.util.Optional;

import org.Pratica_SpringBoot.Models.Entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByCodigoCurso(String codigoCurso);
}