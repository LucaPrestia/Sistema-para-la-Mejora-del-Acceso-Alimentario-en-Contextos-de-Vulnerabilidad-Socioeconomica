package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Tecnico;
import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    Optional<Tecnico> findByUsuario(Usuario usuario);
}
