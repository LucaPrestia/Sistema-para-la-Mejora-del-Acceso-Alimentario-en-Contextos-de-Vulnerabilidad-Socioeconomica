package ar.utn.sistema.entities.reporte;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class ReporteViandasColaborador extends PersistenciaID {
    /*
    - colaborador: Colaborador
    - cantViandasDonadas: int
    - fechaReporte: LocalDate
    */
    @OneToOne
    private Colaborador colaborador;
    private int cantViandasDonadas;
    private LocalDate fechaReporte;

    public ReporteViandasColaborador(Colaborador colaborador, int cantViandasDonadas) {
        this.colaborador = colaborador;
        this.cantViandasDonadas = cantViandasDonadas;
        this.fechaReporte = LocalDate.now();
    }
}
