package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.colaboracion.TipoColaboracion;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
@Getter @Setter @NoArgsConstructor
public abstract class Colaborador extends Rol implements Suscriptor{
    private Usuario usuario; // id_usuario: 5
    private List<Contacto> contactos = new ArrayList<Contacto>();
    @Embedded
    private Direccion direccion;
    private List<Colaboracion> colaboraciones = new ArrayList<Colaboracion>();
    private double puntosDisponibles;
    private List<OfertaCanje> serviciosCanjeados = new ArrayList<OfertaCanje>();
    private List<TipoColaboracion> tiposColaboracion; // los tipos de colaboraciones que seleccionó para realizar
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
