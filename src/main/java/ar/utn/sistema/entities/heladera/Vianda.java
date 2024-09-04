package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.PersistenciaID;
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

    public Vianda(String comida, Date fechaCaducidad, Date fechaDonacion, Heladera heladera, Float calorias, Float peso) {
        this.comida = comida;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaDonacion = fechaDonacion;
        this.heladera = heladera;
        this.calorias = calorias;
        this.peso = peso;
    }
}
