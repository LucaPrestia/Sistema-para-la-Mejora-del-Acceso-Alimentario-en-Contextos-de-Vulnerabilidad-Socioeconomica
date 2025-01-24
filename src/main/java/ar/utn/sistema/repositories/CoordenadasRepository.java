package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Coordenadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordenadasRepository extends JpaRepository<Coordenadas, Integer> {
}