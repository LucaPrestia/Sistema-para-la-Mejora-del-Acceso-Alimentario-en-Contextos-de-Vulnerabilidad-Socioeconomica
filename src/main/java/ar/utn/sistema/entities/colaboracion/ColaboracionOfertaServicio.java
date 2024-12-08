package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboracionOfertaServicio extends Colaboracion {
    @OneToOne
    private OfertaCanje oferta;

    public ColaboracionOfertaServicio(String nombre, RubroServicio rubro, double puntosRequeridos, String imagen, Colaborador colaborador){
        super(TipoColaboracionEnum.OFERTA_SERVICIO, 0.0);
        this.oferta = new OfertaCanje(nombre, rubro, puntosRequeridos, imagen, colaborador);
    }

}
