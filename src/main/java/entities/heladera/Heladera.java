package entities.heladera;
import entities.Direccion;
import entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "heladera")
public class Heladera extends PersistenciaID {
    private String nombre;
    private String owner;
    private Date fechaPuestaFuncionamieto;
    @Embedded
    private Direccion direccion;
    private Estados estado;
    private int maxViandas;

    @OneToMany(mappedBy = "heladera", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}) // RELACION BIODIRECCIONAL!
    private List<Vianda> viandas;
    private int currentCantViandas;

//METODOS
    public void AgragarAListaVianda(Vianda vianda){
        this.viandas.add(vianda);
    }
    public boolean Llena(){
        return this.maxViandas >= currentCantViandas;
    }
}
