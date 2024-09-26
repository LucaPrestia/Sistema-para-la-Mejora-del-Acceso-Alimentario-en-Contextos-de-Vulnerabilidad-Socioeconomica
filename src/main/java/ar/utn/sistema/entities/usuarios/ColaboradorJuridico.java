package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboradorJuridico extends Colaborador {
    private String razonSocial;
    private String rubro;
    private String cuit;
    private TipoJuridico tipoJuridico;
    private Heladera heladera;
}
