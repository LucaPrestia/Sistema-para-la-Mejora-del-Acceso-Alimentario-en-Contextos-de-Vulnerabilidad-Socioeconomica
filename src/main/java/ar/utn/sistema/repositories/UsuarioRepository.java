package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.stereotype.Repository;

// @Repository
// todo: implementar JPARepository
public interface UsuarioRepository {
    Usuario findByUsuario(String usuario);
}
