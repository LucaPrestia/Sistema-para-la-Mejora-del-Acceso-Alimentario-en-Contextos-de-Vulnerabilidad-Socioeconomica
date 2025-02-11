package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {
    Long countByHeladeraAndFechaHoraBetween(Heladera heladera, LocalDateTime fechaHoraDesde, LocalDateTime fechaHoraHasta);
    List<Incidente> findAllByZona(String zona);

    @Query("SELECT i FROM IncidenteFallaTecnica i " +
            "JOIN i.heladera h " +
            "JOIN h.suscriptores s " +
            "WHERE s.id = :idColaborador")
    List<IncidenteFallaTecnica> findFallasBySuscriptor(@Param("idColaborador") Integer idColaborador);

    @Query("SELECT i FROM IncidenteAlerta i " +
            "JOIN i.heladera h " +
            "JOIN h.suscriptores s " +
            "WHERE s.id = :idColaborador")
    List<IncidenteAlerta> findAlertasBySuscriptor(@Param("idColaborador") Integer idColaborador);
}
