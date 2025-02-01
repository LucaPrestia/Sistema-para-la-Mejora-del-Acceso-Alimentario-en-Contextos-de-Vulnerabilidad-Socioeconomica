package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaVulnerableRespository extends JpaRepository<PersonaVulnerable, Integer> {

}
