package ar.utn.sistema.repositories.reportes;

import ar.utn.sistema.entities.reporte.ReporteFallasHeladera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteFallasHeladeraRepository extends JpaRepository<ReporteFallasHeladera, Integer> {
}
