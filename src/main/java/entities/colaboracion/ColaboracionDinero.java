package entities.colaboracion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@DiscriminatorValue(value = "dinero")
public class ColaboracionDinero extends Colaboracion {
    private Float monto;
    private Integer frencuencia; // TODO: evaluar que tipo de dato debería ser

}
