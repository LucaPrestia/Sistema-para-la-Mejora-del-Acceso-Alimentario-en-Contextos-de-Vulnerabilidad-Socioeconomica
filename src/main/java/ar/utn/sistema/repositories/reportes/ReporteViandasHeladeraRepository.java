package ar.utn.sistema.repositories.reportes;

import ar.utn.sistema.entities.reporte.ReporteViandasHeladera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteViandasHeladeraRepository extends JpaRepository<ReporteViandasHeladera, Integer> {
}
