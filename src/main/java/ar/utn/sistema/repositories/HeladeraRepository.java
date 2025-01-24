package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.heladera.Heladera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HeladeraRepository extends JpaRepository<Heladera, Integer> {
}