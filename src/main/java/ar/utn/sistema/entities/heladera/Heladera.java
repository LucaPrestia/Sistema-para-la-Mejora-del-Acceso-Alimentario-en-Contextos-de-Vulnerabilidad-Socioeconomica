package ar.utn.sistema.entities.heladera;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter @Setter @NoArgsConstructor
public class Heladera extends PersistenciaID {
    private String nombre;
    private String owner;
    private Date fechaPuestaFuncionamieto;
    private Direccion direccion;
    private Estados estado;
    private int maxViandas;
    private List<Vianda> viandas = new ArrayList<Vianda>();
    private int currentCantViandas;

//METODOS
    public void AgragarAListaVianda(Vianda vianda){
        this.viandas.add(vianda);
    }
    public boolean Llena(){
        return this.maxViandas >= currentCantViandas;
    }
}
