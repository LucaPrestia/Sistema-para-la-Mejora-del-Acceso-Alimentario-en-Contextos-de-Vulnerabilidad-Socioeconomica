package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColaboracionRepository extends JpaRepository<TipoColaboracion, Integer> {
}
