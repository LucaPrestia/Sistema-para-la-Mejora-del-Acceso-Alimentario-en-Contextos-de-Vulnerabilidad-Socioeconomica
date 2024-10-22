package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Tecnico;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository // todo: implementar interfaz JPA
public interface TecnicoRepository {
    Optional<Tecnico> findByUsuario(Integer id);
}
