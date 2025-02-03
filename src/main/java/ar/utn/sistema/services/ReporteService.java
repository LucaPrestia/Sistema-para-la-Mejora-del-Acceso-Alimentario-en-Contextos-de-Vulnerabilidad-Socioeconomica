package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.TipoMovimientoVianda;
import ar.utn.sistema.entities.reporte.ReporteFallasHeladera;
import ar.utn.sistema.entities.reporte.ReporteViandasColaborador;
import ar.utn.sistema.entities.reporte.ReporteViandasHeladera;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.reportes.ReporteFallasHeladeraRepository;
import ar.utn.sistema.repositories.reportes.ReporteViandasColaboradorRepository;
import ar.utn.sistema.repositories.reportes.ReporteViandasHeladeraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ColaboradorRepository rColaborador;
    @Autowired
    private HeladeraRepository rHeladera;
    @Autowired
    private IncidenteRepository rIncidente;
    @Autowired
    private MovimientoViandaRepository rMovimientoVianda;
    @Autowired
    private ColaboracionRepository rColaboracion;
    @Autowired
    private ReporteViandasColaboradorRepository rReporteViandasColaborador;
    @Autowired
    private ReporteFallasHeladeraRepository rReporteFallasHeladera;
    @Autowired
    private ReporteViandasHeladeraRepository rReporteViandasHeladera;

    @Scheduled(cron = "0 0 0 * * SUN")  // define un job para generar los reportes que se ejecuta todos los domingos a la medianoche
    public void generarReportesSemanales() {
        rReporteFallasHeladera.deleteAll();
        rReporteViandasHeladera.deleteAll();
        rReporteViandasColaborador.deleteAll();
        generarReporteFallasPorHeladera();
        generarReporteViandasPorHeladera();
        generarReporteViandasPorColaborador();
    }

    private void generarReporteViandasPorColaborador() {
        List<Colaborador> colaboradores = rColaborador.findAll();
        for (Colaborador colaborador : colaboradores) {
            Long viandasDonadas = rColaborador.getTotalViandasPorColaborador(colaborador.getId());

            ReporteViandasColaborador reporte = new ReporteViandasColaborador(colaborador,viandasDonadas);

            rReporteViandasColaborador.save(reporte);
        }
    }

    private void generarReporteViandasPorHeladera() {
        List<Heladera> heladeras = rHeladera.findAll();
        for (Heladera heladera : heladeras) {
            Long viandasRetiradas = rMovimientoVianda
                .countByOrigenHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimientoIn (
                    heladera,
                    LocalDateTime.now().minusWeeks(1),
                    LocalDateTime.now(),
                    Arrays.asList(TipoMovimientoVianda.CONSUMO, TipoMovimientoVianda.REDISTRIBUCION)
             );
            Long viandasColocadas = rMovimientoVianda
                .countByOrigenHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimiento(
                    heladera,
                    LocalDateTime.now().minusWeeks(1),
                    LocalDateTime.now(),
                    TipoMovimientoVianda.DONACION
             ) + rMovimientoVianda
                .countByDestinoHeladeraAndFechaHoraMovimientoBetweenAndTipoMovimiento(
                    heladera,
                    LocalDateTime.now().minusWeeks(1),
                    LocalDateTime.now(),
                    TipoMovimientoVianda.REDISTRIBUCION
             );

            ReporteViandasHeladera reporte = new ReporteViandasHeladera(heladera, viandasRetiradas, viandasColocadas);

            rReporteViandasHeladera.save(reporte);
        }
    }

    private void generarReporteFallasPorHeladera() {
        List<Heladera> heladeras = rHeladera.findAll();
        for (Heladera heladera : heladeras) {
            Long cantidadFallas = rIncidente.countByHeladeraAndFechaHoraBetween(heladera, LocalDateTime.now().minusWeeks(1), LocalDateTime.now());

            ReporteFallasHeladera reporte = new ReporteFallasHeladera(heladera, cantidadFallas);

            rReporteFallasHeladera.save(reporte);
        }
    }
}
