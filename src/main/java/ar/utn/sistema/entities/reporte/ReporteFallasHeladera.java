package ar.utn.sistema.entities.reporte;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class ReporteFallasHeladera extends PersistenciaID {
    @ManyToOne
    @JoinColumn(name = "heladeraId", referencedColumnName = "id")
    private Heladera heladera;
    private int cantFallas;
    private LocalDate fechaReporte;

    public ReporteFallasHeladera(Heladera heladera, int cantFallas) {
        this.heladera = heladera;
        this.cantFallas = cantFallas;
        this.fechaReporte = LocalDate.now();
    }
}
