package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.model.TipoCampo;
import lombok.Getter;

@Getter
public enum TipoJuridico {
    GUBERNAMENTAL("Gubernamental"),
    ONG("ONG"),
    EMPRESA("Empresa"),
    INSTITUCION("Institución");

    private final String value;

    TipoJuridico(String value){this.value = value;}
}
