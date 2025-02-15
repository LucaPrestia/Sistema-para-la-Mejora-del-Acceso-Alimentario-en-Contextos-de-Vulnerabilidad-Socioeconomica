package ar.utn.sistema.entities.heladera;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Suscriptor;
import ar.utn.sistema.entities.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class Heladera extends PersistenciaID {

    private String nombre;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Usuario owner;
    private LocalDate fechaPuestaFuncionamiento;

    @OneToOne(cascade = CascadeType.ALL)
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    private EstadoHeladera estado;

    private int maxViandas;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_heladera",referencedColumnName = "id")
    private List<Vianda> viandas = new ArrayList<Vianda>();

    private double tempMin;
    private double tempMax;
    private double ultTempRegs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "heladera_suscriptor", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_heladera"), // Columna que referencia a esta entidad
            inverseJoinColumns = @JoinColumn(name = "id_suscriptor") // Columna que referencia a la entidad relacionada
    )
    private List<Colaborador> suscriptores;
    private Integer permisoApertura; // 1: si, 0: no

    // Constructor
    public Heladera(String nombre, Usuario owner, Direccion direccion, double tempMax, double tempMin, int maxViandas) {
        this.nombre = nombre;
        this.owner = owner;
        this.direccion = direccion;
        this.fechaPuestaFuncionamiento = LocalDate.now();
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.maxViandas = maxViandas;
        this.estado = EstadoHeladera.ACTIVA;
        this.permisoApertura = 0;
    }

    // Métodos

    public boolean llena(){
        return this.maxViandas == viandas.size();
    }

    public int mesesActiva(){
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(this.fechaPuestaFuncionamiento, fechaActual);
        return periodo.getYears() * 12 + periodo.getMonths();
    }

    public void suscribir(Colaborador s){
        this.suscriptores.add(s);
    }

    public void  desuscribir(Colaborador s){
        this.suscriptores.remove(s);
    }

    public void agregarSuscriptor(Colaborador colaborador){
        this.suscriptores.add(colaborador);
    }

    public int espacioDisponibleViandas(){
        return this.maxViandas - this.viandas.size();
    }


}
