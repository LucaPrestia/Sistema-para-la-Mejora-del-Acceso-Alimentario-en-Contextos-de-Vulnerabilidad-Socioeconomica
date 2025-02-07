package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IncidenteAlertaRepository extends JpaRepository<IncidenteAlerta, Integer> {
    List<IncidenteAlerta> findAllByHeladeraIsIn(List<Heladera> heladera);
}