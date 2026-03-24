package org.Pratica_SpringBoot.Repositories;

import java.util.Optional;

import org.Pratica_SpringBoot.Models.Entities.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, Long> {

	Optional<Estudante> findByCpf(String cpf);
}