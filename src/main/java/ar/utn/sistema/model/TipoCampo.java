package ar.utn.sistema.model;

import lombok.Getter;

@Getter
public enum TipoCampo {
    TEXTO("TEXTO"),
    NUMERO("NUMERO"),
    CHECKBOX("CHECKBOX"),
    FECHA("FECHA"),
    SELECCION("SELECCION");

    private final String value;

    TipoCampo(String value) {
        this.value = value;
    }

}
