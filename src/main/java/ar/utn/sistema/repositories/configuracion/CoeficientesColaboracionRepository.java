package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoeficientesColaboracionRepository extends JpaRepository<CoeficientesColaboracion, Integer> {

    Optional<CoeficientesColaboracion> findByTipoColaboracion(TipoColaboracion tipoColaboracion);
}
