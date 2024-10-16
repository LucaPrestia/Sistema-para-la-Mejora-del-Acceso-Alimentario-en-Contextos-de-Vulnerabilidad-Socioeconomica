package ar.utn.sistema.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class MensajeTemperatura implements Serializable { // Implementa Serializable
    private static final long serialVersionUID = 1L; // Asegúrate de agregar un serialVersionUID

    private Integer heladeraId;
    private double temperatura;

    @JsonCreator
    public MensajeTemperatura(@JsonProperty("heladeraId") Integer heladeraId, @JsonProperty("temperatura") double temperatura) {
        this.heladeraId = heladeraId;
        this.temperatura = temperatura;
    }
}
