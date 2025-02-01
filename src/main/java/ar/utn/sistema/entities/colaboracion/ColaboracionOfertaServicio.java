package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "colaboracion_oferta_servicio")
public class ColaboracionOfertaServicio extends Colaboracion {
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private OfertaCanje oferta;

    public ColaboracionOfertaServicio(String nombre, RubroServicio rubro, double puntosRequeridos, byte[] imagen, Colaborador colaborador, Double coeficiente){
        super(TipoColaboracionEnum.OFERTA_SERVICIO, coeficiente);
        this.oferta = new OfertaCanje(nombre, rubro, puntosRequeridos, imagen, colaborador);
    }

}
