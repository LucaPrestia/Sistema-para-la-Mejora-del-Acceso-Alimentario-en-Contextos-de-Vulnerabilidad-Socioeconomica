package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.Colaborador;

import java.time.Duration;
import java.time.LocalDateTime;

public class TarjetaColaborador extends Tarjeta{

    private int tiempoMovimientoApertura = 3; // todo: tomar este valor de la base de datos luego
    private Colaborador colaborador;

    @Override
    public boolean usarTarjeta(Heladera heladera) {
        MovimientoTarjeta movimientoPendiente = buscarMovimiento(heladera);
        if (movimientoPendiente != null){
            movimientoPendiente.setFechaApertura(LocalDateTime.now());
            this.autorizarAperturaHeladera(heladera);
            return true;
        } else return false; // todo: ver si se maneja el motivo de rechazo desde acá o desde el controller
    }

    // el pedido de apertura lo estamos guardando como un movimiento pendiente en la tarjeta: todavía no tiene cargada una fecha de apertura!
    public void registrarPedido(Heladera heladera, MotivoMovimientoTarjeta motivo, int cantidadViandas){
        MovimientoTarjeta pedidoMovimiento = new MovimientoTarjeta(heladera, motivo, cantidadViandas);
        pedidoMovimiento.setFechaPedidoMovimiento(LocalDateTime.now());
        this.agregarMovimiento(pedidoMovimiento);
    }

    private MovimientoTarjeta buscarMovimiento(Heladera heladera){
        MovimientoTarjeta movimientoTarjeta = this.getMovimientos().stream()
                .filter(p -> p.getHeladera().equals(heladera)
                        // && p.getMotivo().equals(motivo) ANALIZAR SI EL MOTIVO TMB SE DEBE INDICAR EN LA APERTURA O NO
                        && p.getFechaApertura() == null  // la apertura aún no se realizó
                        && p.getFechaPedidoMovimiento() != null // hay una fecha de pedido registrada para dicho movimiento
                        && Duration.between(p.getFechaPedidoMovimiento(), LocalDateTime.now()).toHours() <= tiempoMovimientoApertura)
                        // no pasaron más de 3 horas desde el pedido de apertura
                .findFirst()
                .get();
        return movimientoTarjeta;
    }
}
