package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.usuarios.Ong;
import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
}