package ar.utn.sistema.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CampoFormulario {
    private String nombreCampo;
    private String etiqueta;
    private String tipo;
    private boolean obligatorio;
    private boolean requerido;

    // Constructor
    public CampoFormulario(String nombreCampo, String etiqueta, String tipo, boolean requerido) {
        this.nombreCampo = nombreCampo;
        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.requerido = requerido;
        this.obligatorio = false; // los valores obligatorios ya están definidos por defecto en el JSON y la ONG no podrá cambiarlos
    }

    public boolean getObligatorio() {
        return this.obligatorio;
    }
}
