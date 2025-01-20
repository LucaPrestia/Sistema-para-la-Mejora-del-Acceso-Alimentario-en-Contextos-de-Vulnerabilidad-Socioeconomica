package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {
    // Optional<Colaborador> findByUsuario(Integer id);
    Optional<ColaboradorFisico> findByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);

    // dos formas distintas de buscarlo, ambas válidas:
    Optional<Colaborador> findByUsuario(Usuario usuario); // busqueda con el objeto usuario
    Optional<Colaborador> findByUsuario_Id(Integer id); // busqueda con id del usuario
}
