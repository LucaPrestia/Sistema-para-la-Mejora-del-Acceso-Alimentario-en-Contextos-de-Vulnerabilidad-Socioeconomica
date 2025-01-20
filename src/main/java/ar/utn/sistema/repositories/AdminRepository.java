package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Ong;
import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Ong, Integer> {
    Optional<Ong> findByUsuario(Usuario usuario);
}
