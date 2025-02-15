package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.tarjeta.Tarjeta;
import ar.utn.sistema.entities.tarjeta.TarjetaColaborador;
import ar.utn.sistema.entities.tarjeta.TarjetaPersonaVulnerable;
import ar.utn.sistema.entities.usuarios.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {
    Optional<TarjetaColaborador> findByColaborador(Colaborador colaborador);
}
