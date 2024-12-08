package ar.utn.sistema.entities.colaboracion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboracionDinero extends Colaboracion {
    @Column()
    private Float monto;
    @Enumerated(EnumType.STRING)
    private TipoFrecuencia frecuencia;

    public ColaboracionDinero(Float monto, TipoFrecuencia frecuencia){
        super(TipoColaboracionEnum.DINERO, 0.5);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.monto = monto;
        this.frecuencia = frecuencia;
    }

    public ColaboracionDinero(Float montoCargaMasiva, LocalDate fechaCargaMasiva){
        super(TipoColaboracionEnum.DINERO, 0.5);
        this.monto = montoCargaMasiva;
        this.frecuencia = TipoFrecuencia.UNICA;
        this.setFechaColaboracion(fechaCargaMasiva);
    }

    public void donarDinero(){
        // TODO
    }

    @Override
    public double sumarPuntos() {
        return this.frecuencia == TipoFrecuencia.UNICA ? this.monto * this.getCoeficientePuntos() : 0;
    }
}
