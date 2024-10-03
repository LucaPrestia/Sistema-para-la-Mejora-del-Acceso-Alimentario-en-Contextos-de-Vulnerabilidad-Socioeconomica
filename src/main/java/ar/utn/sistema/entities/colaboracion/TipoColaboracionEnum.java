package ar.utn.sistema.entities.colaboracion;

import lombok.Getter;

@Getter
public enum TipoColaboracionEnum {
    DINERO("Donación Dinero"),
    DONACION_VIANDAS("Donacion Viandas"),
    REDISTRIBUCION_VIANDAS("Distribución Viandas"),
    ENTREGA_TARJETAS("Entrega Tarjetas"),
    GESTION_HELADERA("Gestion Heladeras"),
    OFERTA_SERVICIO("Oferta Servicio");

    private final String value;

    TipoColaboracionEnum(String value) {
        this.value = value;
    }

}
