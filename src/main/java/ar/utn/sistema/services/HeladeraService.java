package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.*;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.incidente.TipoAlerta;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Suscriptor;
import ar.utn.sistema.model.MensajeTemperatura;
import ar.utn.sistema.repositories.HeladeraRepository;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HeladeraService {

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private HeladeraRepository heladeraRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private ContactoService contactoService;

    public List<Heladera> obtenerTodasLasHeladeras() {
       return heladeraRepository.findByEstado(EstadoHeladera.ACTIVA);
    }

    // Receive temperature sensor messages
    // @RabbitListener(queues = "temperatura.heladera")
    @Transactional
    public void registrarTemperatura(MensajeTemperatura mensaje) throws IOException {
        Heladera heladera = heladeraRepository.findWithSuscriptoresById(mensaje.getHeladeraId()).get();
        double temperatura = mensaje.getTemperatura();
        heladera.setUltTempRegs(temperatura);

        if (temperatura < heladera.getTempMin() || temperatura > heladera.getTempMax()) {
            String temperaturaStr = (temperatura < heladera.getTempMin()) ? "baja" : "alta";
            String mensajeNotificacion = "Alerta de temperatura " + temperaturaStr +
                    " ( " + temperatura + "°C) en la heladera '" + heladera.getNombre() +
                    "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
            System.out.println("pasaste direccion");
            registrarAlerta(heladera, TipoAlerta.TEMPERATURA, mensajeNotificacion, 1);
        } else {
            heladeraRepository.save(heladera); // registra última temperatura registrada
        }
    }

    // Receive movement sensor messages
    // @RabbitListener(queues = "movimiento.heladera", priority = "10") // higher priority
    @Transactional
    public void registrarMovimiento(Integer heladeraId) throws IOException {
        Heladera heladera = heladeraRepository.findWithSuscriptoresById(heladeraId).get();
        String mensajeNotificacion = "Alerta de movimiento, posible fraude en la heladera '" + heladera.getNombre() +
                "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
        registrarAlerta(heladera, TipoAlerta.FRAUDE, mensajeNotificacion, 0);
    }

    @Transactional
    public void registrarFalloConexion(Integer heladeraId) throws IOException {
        Heladera heladera = heladeraRepository.findWithSuscriptoresById(heladeraId).get();
        String mensajeNotificacion = "Alerta por fallo de conexión en la heladera '" + heladera.getNombre() + "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
        registrarAlerta(heladera, TipoAlerta.CONEXION, mensajeNotificacion, 1);
    }

    private void registrarAlerta(Heladera heladera, TipoAlerta motivo, String mensajeNotif, int correspondeNotificarTecnico) throws IOException {
        heladera.setEstado(EstadoHeladera.INACTIVA);
        Incidente incidente = new IncidenteAlerta(LocalDateTime.now(), heladera, motivo);
        System.out.println("create alerta pero todavía no notificaste");
        notificarDesperfecto(heladera, mensajeNotif, correspondeNotificarTecnico);
        incidenteRepository.save(incidente);
        heladeraRepository.save(heladera);
    }

    public boolean agregarViandas(Heladera heladera, List<Vianda> viandasNuevas) throws IOException {
        if (heladera.getPermisoApertura() == 1) {
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
            notificarSuscriptor(heladera, mensaje.toString(), PreferenciaNotificacion.HELADERA_LLENA, espacioViandasDisponibles);
            heladera.setPermisoApertura(0);
            heladeraRepository.save(heladera); // guarda heladera y viandas registradas
            return true;
        } else return false;
    }

    public boolean sacarViandas(Heladera heladera, List<Vianda> viandas) throws IOException {
        if (heladera.getPermisoApertura() == 1) {
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
            notificarSuscriptor(heladera, mensaje.toString(), PreferenciaNotificacion.POCAS_VIANDAS, cantidadViandasDisponibles);
            heladera.setPermisoApertura(0);
            heladeraRepository.save(heladera);
            return true;
        } else return false;
    }

    public void consumirVianda(Heladera heladera, Vianda vianda) throws IOException {
        MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.CONSUMO, heladera, null, null);
        vianda.agregarMovimientoVianda(movimiento);
        sacarViandas(heladera, List.of(vianda));
    }

    public void notificarDesperfecto(Heladera heladera, String mensaje, int correspondeNotificarTecnico) throws IOException {
        notificarSuscriptor(heladera, mensaje, PreferenciaNotificacion.DESPERFECTO, 0);
        System.out.println("notificaste suscriptores");
        if(correspondeNotificarTecnico == 1) {
            // todo: notificar tecnico más cercano!!
            System.out.println("notificaste tecnicos");
        }
    }

    private void notificarSuscriptor(Heladera heladera, String mensaje, PreferenciaNotificacion pref, int cantidadViandas) throws IOException {
        for (Suscriptor s: heladera.getSuscriptores()){
            if (s.correspondeVerificar(pref, cantidadViandas)){
                if (s instanceof Colaborador colaborador) {
                    Notificacion notificacion = new Notificacion(mensaje);
                    contactoService.inicializarMediosDeContacto(colaborador.getContactos());
                    s.notificar(notificacion);
                    notificacionRepository.save(notificacion);
                }
            }
        }
    }
}
