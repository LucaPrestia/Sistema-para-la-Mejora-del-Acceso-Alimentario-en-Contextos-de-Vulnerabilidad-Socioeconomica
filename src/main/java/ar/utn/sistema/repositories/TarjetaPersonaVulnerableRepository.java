package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.tarjeta.TarjetaPersonaVulnerable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarjetaPersonaVulnerableRepository extends JpaRepository<TarjetaPersonaVulnerable, Integer> {

}
