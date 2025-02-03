package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.MovimientoVianda;
import ar.utn.sistema.entities.heladera.TipoMovimientoVianda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoViandaRepository extends JpaRepository<MovimientoVianda, Integer> {
    Long countByOrigenHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimientoIn(Heladera heladera,
                                                                           LocalDateTime FechaMovimientoDesde,
                                                                               LocalDateTime FechaMovimientoHasta,
                                                                           List<TipoMovimientoVianda> tipos);
    Long countByOrigenHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimiento(Heladera heladera,
                                                                             LocalDateTime FechaMovimientoDesde,
                                                                             LocalDateTime FechaMovimientoHasta,
                                                                         TipoMovimientoVianda tipo);
    Long countByDestinoHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimiento(Heladera heladera,
                                                                              LocalDateTime FechaMovimientoDesde,
                                                                              LocalDateTime FechaMovimientoHasta,
                                                                              TipoMovimientoVianda tipo);


}

