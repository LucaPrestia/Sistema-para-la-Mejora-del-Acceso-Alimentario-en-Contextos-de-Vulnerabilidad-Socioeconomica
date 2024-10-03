package ar.utn.sistema.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MensajeTemperatura {
    private Integer heladeraId;
    private double temperatura;
    public MensajeTemperatura(Integer heladeraId, double temperatura) {
        this.heladeraId = heladeraId;
        this.temperatura = temperatura;
    }
}
