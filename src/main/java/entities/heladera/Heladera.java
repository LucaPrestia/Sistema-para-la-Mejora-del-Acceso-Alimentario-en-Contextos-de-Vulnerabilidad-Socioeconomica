package entities.heladera;
import entities.Direccion;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;


@Getter
@Setter

public class Heladera {
    private String nombre;
    private String owner;
    private Date fechaPuestaFuncionamieto;
    private Direccion direccion;
    private Estados estado;
    private int maxViandas;
    private List vianda;
    private int currentCantViandas;

//METODOS
    public void AgragarAListaVianda(int vianda){
        this.vianda.add(vianda);
    }
    public boolean Llena(){
        return this.maxViandas >= currentCantViandas;
    }
}
