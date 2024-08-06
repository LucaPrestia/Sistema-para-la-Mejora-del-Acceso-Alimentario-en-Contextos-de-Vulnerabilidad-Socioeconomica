package entities.colaboracion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboracionDinero extends Colaboracion {
    private Float monto;
    private Integer frencuencia; // TODO: evaluar que tipo de dato debería ser

}
