package ar.utn.sistema.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CampoFormulario {
    private String nombreCampo;
    private String etiqueta;
    private TipoCampo tipo;
    private boolean obligatorio;

    // Constructor
    public CampoFormulario(String nombreCampo, String etiqueta, TipoCampo tipo, boolean obligatorio) {
        this.nombreCampo = nombreCampo;
        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.obligatorio = obligatorio;
    }
}
