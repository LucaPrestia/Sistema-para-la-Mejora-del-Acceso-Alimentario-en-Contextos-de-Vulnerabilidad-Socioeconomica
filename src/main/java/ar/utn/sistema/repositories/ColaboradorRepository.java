package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Colaborador;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository // todo: implementar interfaz JPA
public interface ColaboradorRepository {
    Optional<Colaborador> findByUsuario(Integer id);
}
