package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class TarjetaPersonaVulnerable extends Tarjeta{

    private static final int cantUsosXDia = 4;
    private static final int cantUsosXMenor = 2;
    @OneToOne
    private PersonaVulnerable personaVulnerable;
    @OneToOne
    private ColaboradorFisico registrador;

    public TarjetaPersonaVulnerable(String codigo) {
        super(codigo);
    }

    @Override
    public boolean usarTarjeta(Heladera heladera) {
        if (puedeUsarTarjeta()){
            MovimientoTarjeta movimiento = new MovimientoTarjeta(heladera, MotivoMovimientoTarjeta.CONSUMO_VIANDA, 1);
            // según el enunciado: una vianda por uso de tarjeta!
            LocalDateTime fechaHora = LocalDateTime.now();
            movimiento.setFechaApertura(fechaHora);
            movimiento.setFechaPedidoMovimiento(fechaHora);
            this.agregarMovimiento(movimiento);
            this.autorizarAperturaHeladera(heladera);
            return true;
        } else return false; // todo: ver si se maneja el motivo de rechazo desde acá o desde el controller
    }

    private int usosDeTarjetaHoy(){
        int contador = 0;
        LocalDate hoy = LocalDate.now(); // lo cambie a LocalDateTimeConverter.java por el nuevo requerimiento en la entrega 3 donde pide comparar cantidad de horas
        List<LocalDateTime> fechasDeUso = getMovimientos().stream().map(x -> x.getFechaApertura()).toList();

        for (LocalDateTime fecha : fechasDeUso)
            if (fecha.toLocalDate().isEqual(hoy)) contador++;

        return contador;
    }

    public boolean puedeUsarTarjeta(){
        return this.usosDeTarjetaHoy() < personaVulnerable.getMenoresACargo() * cantUsosXMenor + cantUsosXDia ;
    }

}
