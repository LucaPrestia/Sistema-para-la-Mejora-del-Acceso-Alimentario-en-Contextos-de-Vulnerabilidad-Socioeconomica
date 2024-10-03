package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public abstract class Colaboracion extends PersistenciaID {
    // private Colaborador colaborador; relacion desde Colaborador
    private LocalDate fechaColaboracion;
    private TipoColaboracionEnum tipo;
    private Double coeficientePuntos;
    private Boolean viejo = false;

    public Colaboracion(TipoColaboracionEnum tipo, Double coeficientePuntos) {
        this.tipo = tipo;
        this.coeficientePuntos = coeficientePuntos;
        this.fechaColaboracion = LocalDate.now();
    }

    public double sumarPuntos(){
        return this.coeficientePuntos;
    }
}
