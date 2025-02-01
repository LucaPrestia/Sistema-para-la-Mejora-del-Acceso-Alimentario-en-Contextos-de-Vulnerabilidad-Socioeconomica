package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class OfertaCanje extends PersistenciaID {
    private String nombre;
    @Enumerated(EnumType.STRING)
    private RubroServicio rubro;
    private double puntosRequeridos;
    @Lob // Define que este campo almacenará datos binarios grandes
    @Column(columnDefinition = "VARBINARY(MAX)") // Compatible con SQL Server
    private byte[] imagen;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Colaborador colaborador;

    public OfertaCanje(String nombre, RubroServicio rubro, double puntosRequeridos, byte[] imagen, Colaborador colaborador) {
        this.nombre = nombre;
        this.rubro = rubro;
        this.puntosRequeridos = puntosRequeridos;
        this.imagen = imagen;
        this.colaborador = colaborador;
    }
}
