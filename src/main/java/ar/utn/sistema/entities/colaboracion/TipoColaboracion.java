package ar.utn.sistema.entities.colaboracion;

import lombok.Getter;

@Getter
public enum TipoColaboracion {
    DONACION_DINERO("DINERO"),
    DONACION_VIANDA("DONACION_VIANDAS"),
    DISTRUBUCION_VIANDA("REDISTRIBUCION_VIANDAS"),
    GESTION_HELADERA("GESTION_HELADERA"),
    REGISTRO_PERSONA_VULNERABLE("REGISTRO_PERSONA_VULNERABLE"),
    OFERTA_SERVICIO("OFERTA_SERVICIO"),
    ENTREGA_TARJETAS("ENTREGA_TARJETAS");

    private final String value;

    TipoColaboracion(String value) {
        this.value = value;
    }

}
