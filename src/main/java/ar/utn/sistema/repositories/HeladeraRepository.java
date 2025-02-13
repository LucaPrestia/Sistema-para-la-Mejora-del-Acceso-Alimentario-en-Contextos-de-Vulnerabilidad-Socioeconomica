package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HeladeraRepository extends JpaRepository<Heladera, Integer> {
    List<Heladera> findByOwnerIsNull();

    List<Heladera> findBySuscriptoresContains(ColaboradorFisico colaborador);

    List<Heladera> findByEstado(EstadoHeladera estado);

    @EntityGraph(attributePaths = {"suscriptores"})
    Optional<Heladera> findWithSuscriptoresById(Integer id);

}