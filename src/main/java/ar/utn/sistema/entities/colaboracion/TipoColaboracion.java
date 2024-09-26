package ar.utn.sistema.entities.colaboracion;

import lombok.Getter;

@Getter
public enum TipoColaboracion {
    DINERO("Donación Dinero"),
    DONACION_VIANDAS("Donacion Viandas"),
    REDISTRIBUCION_VIANDAS("Distribución Viandas"),
    GESTION_HELADERA("Gestion Heladeras"),
    OFERTA_SERVICIO("Oferta Servicio"),
    ENTREGA_TARJETAS("Entrega Tarjetas");

    private final String value;

    TipoColaboracion(String value) {
        this.value = value;
    }

}
