package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter@Setter
public class ColaboracionTarjeta extends Colaboracion {
    private List<Tarjeta> tarjetasCreadas = new ArrayList<Tarjeta>();
    private Boolean es_vieja;
    public ColaboracionTarjeta() {}

    public ColaboracionTarjeta(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            this.tarjetasCreadas.add(new Tarjeta());
        }

    }
}
