package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.ParametrosGenerales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametrosGeneralesRepository extends JpaRepository<ParametrosGenerales, Integer> {
}
