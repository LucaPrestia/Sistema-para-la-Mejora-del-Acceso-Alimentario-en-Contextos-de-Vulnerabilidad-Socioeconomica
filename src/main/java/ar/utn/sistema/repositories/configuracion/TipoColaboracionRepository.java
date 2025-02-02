package ar.utn.sistema.repositories.configuracion;

import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoColaboracionRepository extends JpaRepository<TipoColaboracion, Integer> {
    Optional<TipoColaboracion> findByNombre(String string);

    // consulta con JPQL (son más seguras que las SQL ya que se realizan sobre los campos de las clase java y no los de la tabla)
    // devuelve las colaboraciones que pueden optar por realizar cada colaborador
    @Query("SELECT tc FROM ColaboradorColaboracion cc " +
            "JOIN cc.tipoColaboracion tc " +
            "WHERE cc.tipoColaborador = :tipoColaborador")
    List<TipoColaboracion> findByTipoColaborador(@Param("tipoColaborador") String tipoColaborador);

    List<TipoColaboracion> findByCodigoIn(List<String> codigos);

}
