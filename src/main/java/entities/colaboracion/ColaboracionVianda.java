package entities.colaboracion;

import entities.heladera.Vianda;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@DiscriminatorValue(value = "vianda")
public class ColaboracionVianda {
    private Integer vianda; // debería tener FK a VIANDA ??
    public void cargarVianda(){
     //   TODO
    }

}
