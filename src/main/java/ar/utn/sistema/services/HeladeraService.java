package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.*;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.incidente.TipoAlerta;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.Suscriptor;
import ar.utn.sistema.model.MensajeTemperatura;
import ar.utn.sistema.repositories.HeladeraRepository;
import ar.utn.sistema.repositories.IncidenteAlertaRepository;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.NotificacionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeladeraService {

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private HeladeraRepository heladeraRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Heladera> obtenerTodasLasHeladeras() {
       return heladeraRepository.findAll();
    }

    // Receive temperature sensor messages
    // @RabbitListener(queues = "temperatura.heladera")
    public void registrarTemperatura(MensajeTemperatura mensaje) throws IOException {
        Heladera heladera = heladeraRepository.findById(mensaje.getHeladeraId()).get();
        double temperatura = mensaje.getTemperatura();
        heladera.setUltTempRegs(temperatura);

        if (temperatura < heladera.getTempMin() || temperatura > heladera.getTempMax()) {
            String temperaturaStr = (temperatura < heladera.getTempMin()) ? "baja" : "alta";
            String mensajeNotificacion = "Alerta de temperatura " + temperaturaStr +
                    " ( " + temperatura + "°C) en la heladera '" + heladera.getNombre() +
                    "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
            registrarAlerta(heladera, TipoAlerta.TEMPERATURA, mensajeNotificacion);
        } else {
            heladeraRepository.save(heladera); // registra última temperatura registrada
        }
    }

    // Receive movement sensor messages
    // @RabbitListener(queues = "movimiento.heladera", priority = "10") // higher priority
    public void registrarMovimiento(Integer heladeraId) throws IOException {
        Heladera heladera = heladeraRepository.findById(heladeraId).get();
        String mensajeNotificacion = "Alerta de movimiento, posible fraude en la heladera '" + heladera.getNombre() +
                "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
        registrarAlerta(heladera, TipoAlerta.FRAUDE, mensajeNotificacion);
    }

    private void registrarAlerta(Heladera heladera, TipoAlerta motivo, String mensajeNotif) throws IOException {
        heladera.setEstado(EstadoHeladera.INACTIVA);
        Incidente incidente = new IncidenteAlerta(LocalDateTime.now(), heladera, motivo);
        Notificacion notificacion = new Notificacion(mensajeNotif);
        notificarDesperfecto(heladera, notificacion);
        incidenteRepository.save(incidente);
        heladeraRepository.save(heladera);
        notificacionRepository.save(notificacion);
    }

    public void agregarViandas(Heladera heladera, List<Vianda> viandasNuevas) throws IOException {
        // todo: ¿¿ chequea si hay autorizacion de apertura ??
        heladera.getViandas().addAll(viandasNuevas);
        int espacioViandasDisponibles = heladera.getMaxViandas() - heladera.getViandas().size();
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Queda espacio para ")
                .append(espacioViandasDisponibles)
                .append(" vianda/s en la heladera de nombre '")
                .append(heladera.getNombre())
                .append("' ubicada en la dirección ")
                .append(heladera.getDireccion().obtenerCadenaDireccion())
                .append(".");
        Notificacion notificacion = new Notificacion(mensaje.toString());
        notificarSuscriptor(heladera, notificacion, PreferenciaNotificacion.HELADERA_LLENA, espacioViandasDisponibles);
        heladeraRepository.save(heladera); // guarda heladera y viandas registradas
    }

    public void sacarViandas(Heladera heladera, List<Vianda> viandas) throws IOException {
        // todo: ¿¿ chequea si hay autorizacion de apertura ??
        heladera.getViandas().removeAll(viandas);
        int cantidadViandasDisponibles = heladera.getViandas().size();
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Queda/n solamente ")
                .append(cantidadViandasDisponibles)
                .append(" vianda/s disponible/s en la heladera de nombre '")
                .append(heladera.getNombre())
                .append("' ubicada en la dirección ")
                .append(heladera.getDireccion().obtenerCadenaDireccion())
                .append(".");
        Notificacion notificacion = new Notificacion(mensaje.toString());
        notificarSuscriptor(heladera, notificacion, PreferenciaNotificacion.POCAS_VIANDAS, cantidadViandasDisponibles);
        heladeraRepository.save(heladera);
    }

    public void consumirVianda(Heladera heladera, Vianda vianda) throws IOException {
        MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.CONSUMO, heladera, null, null);
        vianda.agregarMovimientoVianda(movimiento);
        sacarViandas(heladera, List.of(vianda));
    }

    public void notificarDesperfecto(Heladera heladera, Notificacion notificacion) throws IOException {
        notificarSuscriptor(heladera, notificacion, PreferenciaNotificacion.DESPERFECTO, 0);
    }

    private void notificarSuscriptor(Heladera heladera, Notificacion notificacion, PreferenciaNotificacion pref, int cantidadViandas) throws IOException {
        for (Suscriptor s: heladera.getSuscriptores()){
            if (s.correspondeVerificar(pref, cantidadViandas)){
                // todo: agregar el medio de seleccion de preferencia para las notificaciones (falta hacerlo y agregarlo a la vista de suscripcion)
                s.notificar(notificacion);
                notificacionRepository.save(notificacion);
            }
        }
    }
}
