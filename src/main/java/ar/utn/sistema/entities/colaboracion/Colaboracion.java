package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "colaboracion")
public abstract class Colaboracion extends PersistenciaID {

    private LocalDate fechaColaboracion;

    @Enumerated(EnumType.STRING)
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
