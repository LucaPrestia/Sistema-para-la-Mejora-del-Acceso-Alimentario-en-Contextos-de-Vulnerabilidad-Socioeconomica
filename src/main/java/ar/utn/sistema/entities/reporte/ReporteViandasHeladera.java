package ar.utn.sistema.entities.reporte;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class ReporteViandasHeladera extends PersistenciaID {
    /*
    - heladera: Heladera
    - cantViandasRetiradas: int
    - cantViandasColocadas: int
    - fechaReporte: LocalDate
    */
    @OneToOne
    private Heladera heladera;
    private int cantViandasRetiradas;
    private int cantViandasColocadas;
    private LocalDate fechaReporte;

    public ReporteViandasHeladera(Heladera heladera, int cantViandasRetiradas, int cantViandasColocadas) {
        this.heladera = heladera;
        this.cantViandasRetiradas = cantViandasRetiradas;
        this.cantViandasColocadas = cantViandasColocadas;
        this.fechaReporte = LocalDate.now();
    }
}
