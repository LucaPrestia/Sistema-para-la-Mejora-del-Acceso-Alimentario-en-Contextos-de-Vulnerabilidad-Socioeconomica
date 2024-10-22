package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Ong;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository // todo: implementar interfaz JPA
public interface AdminRepository {
    Optional<Ong> findByUsuario(Integer id);
}
