package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoeficientesColaboracionRepository extends JpaRepository<CoeficientesColaboracion, Integer> {
}
