package ar.utn.sistema.repositories;

import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColaboracionRepository extends JpaRepository<Colaboracion, Integer> {

}
