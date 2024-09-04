package ar.utn.sistema.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class Formulario {
    private String tipoColaborador;
    private List<CampoFormulario> campos = new ArrayList<CampoFormulario>();

    public Formulario(String tipoColaborador, List<CampoFormulario> campos) {
        this.tipoColaborador = tipoColaborador;
        this.campos = campos;
    }
    public void agregarCampo(CampoFormulario nuevoCampo) {this.campos.add(nuevoCampo);}
}
