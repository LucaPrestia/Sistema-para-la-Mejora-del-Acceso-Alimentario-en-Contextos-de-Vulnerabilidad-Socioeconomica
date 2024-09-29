package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class Vianda extends PersistenciaID {

    private String comida;
    private LocalDate fechaCaducidad;
    private LocalDate fechaDonacion;
    private Heladera heladera;

    private Float calorias;
    private Float peso;
    private EstadoVianda estado;

    public Vianda(String comida, LocalDate fechaCaducidad, Heladera heladera, Float calorias, Float peso) {
        this.comida = comida;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaDonacion = LocalDate.now();
        this.heladera = heladera;
        this.calorias = calorias;
        this.peso = peso;
        this.estado = EstadoVianda.DISPONIBLE;
    }
}
