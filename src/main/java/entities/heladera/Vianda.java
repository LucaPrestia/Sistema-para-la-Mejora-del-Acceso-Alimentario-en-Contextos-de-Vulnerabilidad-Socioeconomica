package entities.heladera;

import entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
public class Vianda extends PersistenciaID {

    private String comida;
    private Date fechaCaducidad;
    private Date fechaDonacion;
    private Heladera heladera;

    private Float calorias;
    private Float peso;
    // private Estado estado;


}
