package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoColaboracionRepository extends JpaRepository<TipoColaboracion, Integer> {
    Optional<TipoColaboracion> findByNombre(String string);
}
