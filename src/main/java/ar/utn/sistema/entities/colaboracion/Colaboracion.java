package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter @NoArgsConstructor
public abstract class Colaboracion extends PersistenciaID {
    // private Colaborador colaborador; relacion desde Colaborador
    private LocalDate fechaColaboracion;
    private TipoColaboracion tipo;
    private Double coeficientePuntos;

    public Colaboracion(TipoColaboracion tipo, Double coeficientePuntos) {
        this.tipo = tipo;
        this.coeficientePuntos = coeficientePuntos;
        this.fechaColaboracion = LocalDate.now();
    }

    public double sumarPuntos(){
        return this.coeficientePuntos;
    }
}
