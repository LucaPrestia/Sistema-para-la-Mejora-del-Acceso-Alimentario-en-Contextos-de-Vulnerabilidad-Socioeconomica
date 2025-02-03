package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {
    Long countByHeladeraAndFechaHoraBetween(Heladera heladera, LocalDateTime fechaHoraDesde, LocalDateTime fechaHoraHasta);
}
