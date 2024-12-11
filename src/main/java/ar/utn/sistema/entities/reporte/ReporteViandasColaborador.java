package ar.utn.sistema.entities.reporte;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colaborador", referencedColumnName = "id")
    private Colaborador colaborador;

    private int cantViandasDonadas;
    private LocalDate fechaReporte;

    public ReporteViandasColaborador(Colaborador colaborador, int cantViandasDonadas) {
        this.colaborador = colaborador;
        this.cantViandasDonadas = cantViandasDonadas;
        this.fechaReporte = LocalDate.now();
    }
}
