package ar.utn.sistema.entities.colaboracion;

import lombok.Getter;

@Getter
public enum TipoColaboracion {
    DONACION_DINERO("DonacionDinero"),
    DONACION_VIANDA("DonacionVianda"),
    DISTRUBUCION_VIANDA("DistribucionVianda"),
    GESTION_HELADERA("GestionHeladera"),
    REGISTRO_PERSONA_VULNERABLE("RegistroPersona"),
    OFERTA_SERVICIO("OfertaServicio");

    private final String value;

    TipoColaboracion(String value) {
        this.value = value;
    }

}
