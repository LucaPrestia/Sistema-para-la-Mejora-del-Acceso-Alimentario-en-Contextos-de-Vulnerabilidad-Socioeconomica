package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.Direccion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter @Setter @NoArgsConstructor
public abstract class Colaborador extends Rol {
    private List<Contacto> contactos = new ArrayList<Contacto>();
    private Direccion direccion;
    private List<Colaboracion> colaboraciones = new ArrayList<Colaboracion>();
    private Map<String, String> camposOpcionales = new HashMap<>();
    private double puntosAcumulados;
    private double puntosDisponibles;
    private List<OfertaCanje> serviciosCanjeados = new ArrayList<OfertaCanje>();

    public void agregarCampo(String nombre, String valor) {
        camposOpcionales.put(nombre, valor);
    }

    public String obtenerCampo(String nombre) {
        return camposOpcionales.get(nombre);
    }

    public void agregarColaboracion(Colaboracion colaboracion){
        this.colaboraciones.add(colaboracion);
        double puntos = colaboracion.sumarPuntos();
        actualizarPuntos(puntos);
    }

    public void canjearServicio(OfertaCanje oferta){
        // todo: validar si el colaborador tiene suficientes puntos acumulados para utilizar el servicio
        serviciosCanjeados.add(oferta);
        puntosDisponibles -= oferta.getPuntosRequeridos();
    }

    private void actualizarPuntos(double puntos) {
        puntosAcumulados += puntos;
        puntosDisponibles += puntos;
    }

    public void agregarContacto(Contacto contacto) {this.contactos.add(contacto);}

    public void notificar(Notificacion notificacion) {
        for (Contacto contacto : contactos) {
            if (contacto.getMedio() == notificacion.getMedio()) {
                notificacion.setContacto(contacto.getContacto());
                contacto.notificar(notificacion);
                break;
            }
        }
    }
}
