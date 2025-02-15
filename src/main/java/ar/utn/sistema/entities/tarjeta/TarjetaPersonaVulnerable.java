package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Entity
@NoArgsConstructor @Getter @Setter
@DiscriminatorValue("persona_vulnerable")
public class TarjetaPersonaVulnerable extends Tarjeta{

    @Transient
    private static final int cantUsosXDia = 4;
    @Transient
    private static final int cantUsosXMenor = 2;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_persona_vulnerable", nullable = true)
    private PersonaVulnerable personaVulnerable;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_colaborador_registrador",  nullable = true)
    private ColaboradorFisico registrador;

    public TarjetaPersonaVulnerable(String codigo) {
        super(codigo);
    }

    @Override
    public boolean usarTarjeta(Heladera heladera, MotivoMovimientoTarjeta motivo) {
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
