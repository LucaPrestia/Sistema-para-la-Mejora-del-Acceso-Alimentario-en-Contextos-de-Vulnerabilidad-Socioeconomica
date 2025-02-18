package ar.utn.sistema.entities.reporte;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_heladera", referencedColumnName = "id")
    private Heladera heladera;

    private Long cantViandasRetiradas;
    private Long cantViandasColocadas;
    private LocalDate fechaReporte;

    public ReporteViandasHeladera(Heladera heladera, Long cantViandasRetiradas, Long cantViandasColocadas) {
        this.heladera = heladera;
        this.cantViandasRetiradas = cantViandasRetiradas;
        this.cantViandasColocadas = cantViandasColocadas;
        this.fechaReporte = LocalDate.now();
    }
}
