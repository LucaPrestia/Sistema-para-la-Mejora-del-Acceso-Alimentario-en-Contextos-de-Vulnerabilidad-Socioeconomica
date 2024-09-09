package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OfertaCanje extends PersistenciaID {
    private String nombre;
    private RubroServicio rubro;
    private double puntosRequeridos;
    private String imagen;
    private Colaborador colaborador;

    public OfertaCanje(String nombre, RubroServicio rubro, double puntosRequeridos, String imagen, Colaborador colaborador) {
        this.nombre = nombre;
        this.rubro = rubro;
        this.puntosRequeridos = puntosRequeridos;
        this.imagen = imagen;
        this.colaborador = colaborador;
    }
}
