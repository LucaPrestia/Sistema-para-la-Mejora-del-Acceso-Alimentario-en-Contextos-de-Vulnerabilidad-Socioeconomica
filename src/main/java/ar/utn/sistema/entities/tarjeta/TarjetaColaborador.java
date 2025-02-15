package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
@Entity
@NoArgsConstructor @Getter @Setter
@DiscriminatorValue("colaborador")
public class TarjetaColaborador extends Tarjeta{
    @Transient
    private int tiempoMovimientoApertura = 3; // todo: tomar este valor de la base de datos luego

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_colaborador", nullable = true)
    private Colaborador colaborador;

    public TarjetaColaborador(String codigo, Colaborador colaborador){
        super(codigo);
        this.colaborador = colaborador;
    }

    @Override
    public boolean usarTarjeta(Heladera heladera, MotivoMovimientoTarjeta motivo) {
        MovimientoTarjeta movimientoPendiente = buscarMovimiento(heladera, motivo);
        if (movimientoPendiente != null){
            movimientoPendiente.setFechaApertura(LocalDateTime.now());
            this.autorizarAperturaHeladera(heladera);
            return true;
        } else return false;
    }

    // el pedido de apertura lo estamos guardando como un movimiento pendiente en la tarjeta: todavía no tiene cargada una fecha de apertura!
    public void registrarPedido(Heladera heladera, MotivoMovimientoTarjeta motivo, int cantidadViandas){
        MovimientoTarjeta pedidoMovimiento = new MovimientoTarjeta(heladera, motivo, cantidadViandas);
        pedidoMovimiento.setFechaPedidoMovimiento(LocalDateTime.now());
        this.agregarMovimiento(pedidoMovimiento);
    }

    private MovimientoTarjeta buscarMovimiento(Heladera heladera, MotivoMovimientoTarjeta motivo){
        MovimientoTarjeta movimientoTarjeta = this.getMovimientos().stream()
                .filter(p -> p.getHeladera().equals(heladera)
                         && p.getMotivo().equals(motivo)
                        && p.getFechaApertura() == null  // la apertura aún no se realizó
                        && p.getFechaPedidoMovimiento() != null // hay una fecha de pedido registrada para dicho movimiento
                        && Duration.between(p.getFechaPedidoMovimiento(), LocalDateTime.now()).toHours() <= tiempoMovimientoApertura)
                        // no pasaron más de 3 horas desde el pedido de apertura
                .findFirst()
                .get();
        return movimientoTarjeta;
    }
}
