package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Colaborador extends Suscriptor{

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; // id_usuario: 5

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_colaborador", nullable = true)
    // se graba la clave en Contacto! puede ser nulo en caso de que el contacto le pertenezca a un tecnico
    private List<Contacto> contactos = new ArrayList<Contacto>();

    @Embedded
    private Direccion direccion;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_colaborador") // esto tiene que ir para que no haga una relación ManyToMany
    private List<Colaboracion> colaboraciones = new ArrayList<Colaboracion>();

    private double puntosDisponibles;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_colaborador")
    private List<OfertaCanje> serviciosCanjeados = new ArrayList<OfertaCanje>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "colaborador_colaboraciones_seleccionadas", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_colaborador"), // Columna que referencia a esta entidad
            inverseJoinColumns = @JoinColumn(name = "id_tipo_colaboracion") // Columna que referencia a la entidad relacionada
    )
    private List<TipoColaboracion> tiposColaboracion; // los tipos de colaboraciones que seleccionó para realizar

    @ElementCollection
    @CollectionTable(name = "preferencias_notificacion", joinColumns = @JoinColumn(name = "suscriptor_id"))
    @MapKeyColumn(name = "preferencia")
    @Column(name = "valor")
    private HashMap<PreferenciaNotificacion, Integer> preferenciasNotif;

    public void agregarColaboracion(Colaboracion colaboracion){
        this.colaboraciones.add(colaboracion);
        double puntos = colaboracion.sumarPuntos();
        actualizarPuntos(puntos);
    }

    public boolean canjearPuntos(OfertaCanje oferta){
        if(oferta.getPuntosRequeridos() > this.puntosDisponibles)
            return false;
        else {
            serviciosCanjeados.add(oferta);
            actualizarPuntos(oferta.getPuntosRequeridos() * (-1));
        }
        return true;
    }

    public void actualizarPuntos(double puntos) {
        puntosDisponibles += puntos;
    }

    public void agregarContacto(Contacto contacto) {this.contactos.add(contacto);}

    public void notificar(Notificacion notificacion) {
        for (Contacto contacto : contactos) {
            if (contacto.getMedio() == notificacion.getMedio()) {
                notificacion.setContacto(contacto.getContacto());
                notificacion.setUsuario(this.usuario);
                contacto.notificar(notificacion);
                break;
            }
        }
    }

    public boolean correspondeVerificar(PreferenciaNotificacion preferencia, int cantidadViandas){
        if(preferenciasNotif.containsKey(preferencia)){
            Integer valor = preferenciasNotif.get(preferencia);
            if(preferencia != PreferenciaNotificacion.DESPERFECTO)
                return cantidadViandas <= valor; // corresponde verificar sólo si la cantidad es menor o igual a la configurada por el colaborador (todo: o solo igual??)
            else
                return true; // para DESPERFECTO siempre notifica
        }
        return false;
    }

    public void registrarFalla(Heladera heladera, String descripcion, byte[] foto){
        // TODO: PERSISTIR INCIDENTE + NOTIFICACION
        IncidenteFallaTecnica incidente = new IncidenteFallaTecnica(LocalDateTime.now(), heladera, this, descripcion, foto );
        String mensaje = "Un colaborador ha registrado una falla técnica en la heladera de nombre '" +
                heladera.getNombre() + "' ubicada en la dirección " +
                heladera.getDireccion().obtenerCadenaDireccion() + " indicando lo siguiente: " + descripcion;
        Notificacion notificacion = new Notificacion(mensaje);
        incidente.notificarTecnico(notificacion);
    }

}
