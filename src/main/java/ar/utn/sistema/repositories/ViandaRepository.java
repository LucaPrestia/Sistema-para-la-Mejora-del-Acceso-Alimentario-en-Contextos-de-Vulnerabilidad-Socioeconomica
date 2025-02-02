package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.heladera.Vianda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViandaRepository extends JpaRepository<Vianda, Integer> {
}