package ar.utn.sistema.repositories.reportes;

import ar.utn.sistema.entities.reporte.ReporteViandasColaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteViandasColaboradorRepository extends JpaRepository<ReporteViandasColaborador, Integer> {
}
