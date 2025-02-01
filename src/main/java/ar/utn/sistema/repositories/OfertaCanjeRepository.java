package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaCanjeRepository extends JpaRepository<OfertaCanje, Integer> {
}