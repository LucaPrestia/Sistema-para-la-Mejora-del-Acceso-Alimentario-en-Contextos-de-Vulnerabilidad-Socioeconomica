package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorColaboracionRepository extends JpaRepository<ColaboradorColaboracion, Integer> {
}
