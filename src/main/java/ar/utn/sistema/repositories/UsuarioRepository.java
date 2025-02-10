package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
   Optional<Usuario> findByUsuario(String username);

    Optional<List<Usuario>> findAllByUsuario(String username);

    Optional<Usuario> findByUsuarioAndContrasena(String username, String contrasena);
}
