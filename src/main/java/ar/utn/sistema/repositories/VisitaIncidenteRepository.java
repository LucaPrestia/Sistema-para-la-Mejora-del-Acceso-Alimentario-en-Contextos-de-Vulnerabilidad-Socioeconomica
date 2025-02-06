package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.incidente.VisitaIncidente;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaIncidenteRepository extends JpaRepository<VisitaIncidente, Integer> {

}
