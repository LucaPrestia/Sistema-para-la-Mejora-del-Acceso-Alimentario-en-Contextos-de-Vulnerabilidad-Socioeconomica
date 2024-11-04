package ar.utn.sistema.entities.heladera;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Suscriptor;
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
    private String owner;
    private LocalDate fechaPuestaFuncionamiento;
    @Embedded
    private Direccion direccion;
    @Enumerated(EnumType.STRING)
    private EstadoHeladera estado;
    private int maxViandas;
    @OneToMany
    @JoinColumn(name = "heladeraId",referencedColumnName = "id")
    private List<Vianda> viandas = new ArrayList<Vianda>();
    private double tempMin;
    private double tempMax;
    private double ultTempRegs;
    @OneToMany(targetEntity = Colaborador.class)
    @JoinColumn(name = "heladeraId",referencedColumnName = "id")
    private List<Suscriptor> suscriptores;
    // Constructor
    public Heladera(String nombre, String owner, Direccion direccion, double tempMax, double tempMin, int maxViandas) {
        this.nombre = nombre;
        this.owner = owner;
        this.direccion = direccion;
        this.fechaPuestaFuncionamiento = LocalDate.now();
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.maxViandas = maxViandas;
        this.estado = EstadoHeladera.ACTIVA;
    }

    // Métodos
    public void agregarVianda(Vianda vianda){
        // todo: ¿¿ chequea si hay autorizacion de apertura ??
        this.viandas.add(vianda);
        int espacioViandasDisponibles = maxViandas - viandas.size();
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Queda espacio para ")
                .append(espacioViandasDisponibles)
                .append(" vianda/s en la heladera de nombre '")
                .append(nombre)
                .append("' ubicada en la dirección ")
                .append(direccion.obtenerCadenaDireccion())
                .append(".");
        Notificacion notificacion = new Notificacion(mensaje.toString());
        notificarSuscriptor(notificacion, PreferenciaNotificacion.HELADERA_LLENA, espacioViandasDisponibles);
    }

    public void sacarVianda(Vianda vianda){
        // todo: ¿¿ chequea si hay autorizacion de apertura ??
        this.viandas.remove(vianda);
        int cantidadViandasDisponibles = viandas.size();
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Queda/n solamente ")
                .append(cantidadViandasDisponibles)
                .append(" vianda/s disponible/s en la heladera de nombre '")
                .append(nombre)
                .append("' ubicada en la dirección ")
                .append(direccion.obtenerCadenaDireccion())
                .append(".");
        Notificacion notificacion = new Notificacion(mensaje.toString());
        notificarSuscriptor(notificacion, PreferenciaNotificacion.POCAS_VIANDAS, cantidadViandasDisponibles);
    }


    public void notificarDesperfecto(Notificacion notificacion){
        notificarSuscriptor(notificacion, PreferenciaNotificacion.DESPERFECTO, 0);
    }

    public boolean llena(){
        return this.maxViandas == viandas.size();
    }

    public int mesesActiva(){
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(this.fechaPuestaFuncionamiento, fechaActual);
        return periodo.getYears() * 12 + periodo.getMonths();
    }

    public void suscribir(Suscriptor s){
        this.suscriptores.add(s);
    }

    public void  desuscribir(Suscriptor s){
        this.suscriptores.remove(s);
    }

    private void notificarSuscriptor(Notificacion notificacion, PreferenciaNotificacion pref, int cantidadViandas){
        for (Suscriptor s: suscriptores){
            if (s.correspondeVerificar(pref, cantidadViandas)){
                s.notificar(notificacion);
            }
        }
    }


}
