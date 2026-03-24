package org.Pratica_SpringBoot.Repositories;

import java.util.List;
import java.util.Optional;

import org.Pratica_SpringBoot.Models.Entities.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findByEstudanteIdAndDisciplinaIdAndSemestre(Long estudanteId, Long disciplinaId, Integer semestre);

    List<Matricula> findByEstudanteId(Long estudanteId);

    List<Matricula> findByDisciplinaId(Long disciplinaId);
}