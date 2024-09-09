package ar.utn.sistema.entities.heladera;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter @Setter @NoArgsConstructor
public class Heladera extends PersistenciaID {
    private String nombre;
    private String owner;
    private LocalDate fechaPuestaFuncionamiento;
    private Direccion direccion;
    private EstadoHeladera estado;
    private int maxViandas;
    private List<Vianda> viandas = new ArrayList<Vianda>();
    private double tempMin;
    private double tempMax;
    private double ultTempRegs;

    // Constructor
    public Heladera(String nombre, String owner, Direccion direccion, EstadoHeladera estado, double tempMax, double tempMin, int maxViandas) {
        this.nombre = nombre;
        this.owner = owner;
        this.direccion = direccion;
        this.fechaPuestaFuncionamiento = LocalDate.now();
        this.estado = estado;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.maxViandas = maxViandas;
    }

    // Métodos
    public void AgragarAListaVianda(Vianda vianda){
        this.viandas.add(vianda);
    }
    public boolean Llena(){
        return this.maxViandas == viandas.size();
    }
    public int mesesActiva(){
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(this.fechaPuestaFuncionamiento, fechaActual);
        return periodo.getYears() * 12 + periodo.getMonths();
    }


}