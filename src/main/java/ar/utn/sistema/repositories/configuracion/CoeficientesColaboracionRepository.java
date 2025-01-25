package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoeficientesColaboracionRepository extends JpaRepository<CoeficientesColaboracion, Integer> {
    List<CoeficientesColaboracion> findByTipoColaboracionEquals(String tipoColaboracion);
}
