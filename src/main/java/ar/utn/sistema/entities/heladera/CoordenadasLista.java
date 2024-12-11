package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class CoordenadasLista extends PersistenciaID {
    @OneToMany
    private List<Coordenadas> coordenadas;
}
