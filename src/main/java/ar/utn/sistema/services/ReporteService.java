package ar.utn.sistema.services;

import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.TipoMovimientoVianda;
import ar.utn.sistema.entities.reporte.ReporteFallasHeladera;
import ar.utn.sistema.entities.reporte.ReporteViandasColaborador;
import ar.utn.sistema.entities.reporte.ReporteViandasHeladera;
import ar.utn.sistema.entities.usuarios.Colaborador;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReporteService {
    @Scheduled(cron = "0 0 0 * * SUN")  // define un job para generar los reportes que se ejecuta todos los domingos a la medianoche
    public void generarReportesSemanales() {
        // todo: dropear tablas de reportes para liberar espacio y poder cargar los nuevos reportes de la ultima semana
        // Lógica para generar los reportes
        generarReporteFallasPorHeladera();
        generarReporteViandasPorHeladera();
        generarReporteViandasPorColaborador();
    }

    private void generarReporteViandasPorColaborador() {
        List<Colaborador> colaboradores = new ArrayList<Colaborador>(); //todo: buscar en BD colaboradorRepository.findAll();
        for (Colaborador colaborador : colaboradores) {
            int viandasDonadas = 0; // todo: buscar en BD colaboracionRepository.countByColaboradorIdAndTipo(colaborador.getId(), TipoColaboracionEnum.DONACION_VIANDAS);

            ReporteViandasColaborador reporte = new ReporteViandasColaborador();
            reporte.setColaborador(colaborador);
            reporte.setCantViandasDonadas(viandasDonadas);
            reporte.setFechaReporte(LocalDate.now());

            // todo persistir:reporteViandasColaboradorRepository.save(reporte);
        }
    }

    private void generarReporteViandasPorHeladera() {
        List<Heladera> heladeras = new ArrayList<Heladera>(); //todo: buscar en BD heladeraRepository.findAll();
        for (Heladera heladera : heladeras) {
            int viandasRetiradas = 0;
            /*= movimientoViandaRepository
                .countByHeladeraOrigenAndFechaMovimientoBetweenAndTipoMovimientoIn (
                    heladera,
                    LocalDate.now().minusWeeks(1),
                    LocalDate.now(),
                    Arrays.asList(TipoMovimientoVianda.CONSUMO, TipoMovimientoVianda.REDISTRIBUCION)
             ); */
            int viandasColocadas = 0;
            /*
            = movimientoViandaRepository
                .countByHeladeraOrigenAndFechaMovimientoBetweenAndTipoMovimiento (
                    heladera,
                    LocalDate.now().minusWeeks(1),
                    LocalDate.now(),
                    TipoMovimientoVianda.DONACION
             ) + movimientoViandaRepository
                .countByHeladeraDestinoAndFechaMovimientoBetweenAndTipoMovimiento (
                    heladera,
                    LocalDate.now().minusWeeks(1),
                    LocalDate.now(),
                    TipoMovimientoVianda.REDISTRIBUCION
             )
            */

            ReporteViandasHeladera reporte = new ReporteViandasHeladera();
            reporte.setHeladera(heladera);
            reporte.setCantViandasRetiradas(viandasRetiradas);
            reporte.setCantViandasColocadas(viandasColocadas);
            reporte.setFechaReporte(LocalDate.now());

            // todo persistir: reporteViandasHeladeraRepository.save(reporte);
        }
    }

    private void generarReporteFallasPorHeladera() {
        List<Heladera> heladeras = new ArrayList<Heladera>(); //todo: buscar en BD heladeraRepository.findAll();
        for (Heladera heladera : heladeras) {
            int cantidadFallas = 0;  // todo: incidenteRepository.countByHeladeraIdAndFechaBetween(heladera.getId(), LocalDateTime.now().minusWeeks(1), LocalDateTime.now());

            ReporteFallasHeladera reporte = new ReporteFallasHeladera();
            reporte.setHeladera(heladera);
            reporte.setCantFallas(cantidadFallas);
            reporte.setFechaReporte(LocalDate.now());

            // todo persistir: reporteFallasHeladeraRepository.save(reporte);
        }
    }
}
