package ar.utn.sistema.entities.colaboracion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "colaboracion_dinero")
public class ColaboracionDinero extends Colaboracion {

    private Float monto;

    @Enumerated(EnumType.STRING)
    private TipoFrecuencia frecuencia;

    public ColaboracionDinero(Float monto, TipoFrecuencia frecuencia, Double coeficiente){
        super(TipoColaboracionEnum.DINERO, coeficiente);
        this.monto = monto;
        this.frecuencia = frecuencia;
    }

    public ColaboracionDinero(Float montoCargaMasiva, LocalDate fechaCargaMasiva, Double coeficienteCargaMasiva){
        super(TipoColaboracionEnum.DINERO, coeficienteCargaMasiva);
        this.monto = montoCargaMasiva;
        this.frecuencia = TipoFrecuencia.UNICA;
        this.setFechaColaboracion(fechaCargaMasiva);
    }

    public void donarDinero(){
        // TODO
    }

    @Override
    public double sumarPuntos() {
        return this.monto * this.getCoeficientePuntos() ;
    }
}
