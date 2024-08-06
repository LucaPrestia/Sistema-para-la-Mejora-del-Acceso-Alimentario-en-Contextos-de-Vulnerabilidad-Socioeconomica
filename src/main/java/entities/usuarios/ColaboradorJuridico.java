package entities.usuarios;

import entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboradorJuridico {
    private String razonSocial;
    private TipoJuridico tipoJuridico; // TODO: si se agregan más tipos jurídicos sería conveniente que esto sea una tabla y no un enum
    private Heladera heladera;

}
