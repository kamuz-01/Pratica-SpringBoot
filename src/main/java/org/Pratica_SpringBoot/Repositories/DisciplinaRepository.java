package org.Pratica_SpringBoot.Repositories;

import java.util.List;
import java.util.Optional;

import org.Pratica_SpringBoot.Models.Entities.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    Optional<Disciplina> findByCodigoDisciplina(String codigoDisciplina);

    List<Disciplina> findByCurso_IdCurso(Long idCurso);

    boolean existsByProfessor_Id(Long id);

    boolean existsByCurso_IdCurso(Long idCurso);
}