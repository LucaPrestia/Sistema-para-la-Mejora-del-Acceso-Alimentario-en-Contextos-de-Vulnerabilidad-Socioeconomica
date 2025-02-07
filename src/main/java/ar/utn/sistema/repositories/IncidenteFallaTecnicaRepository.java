package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IncidenteFallaTecnicaRepository extends JpaRepository<IncidenteFallaTecnica, Integer> {
    List<IncidenteFallaTecnica> findAllByHeladeraIsIn(List<Heladera> heladera);
}