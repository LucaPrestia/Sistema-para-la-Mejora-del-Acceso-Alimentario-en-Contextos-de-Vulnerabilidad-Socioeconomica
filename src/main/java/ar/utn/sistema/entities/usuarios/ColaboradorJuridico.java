package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboradorJuridico extends Colaborador {
    private String razonSocial;
    private String rubro;
    private String cuit;
    @Enumerated(EnumType.STRING)
    private TipoJuridico tipoJuridico;
    @OneToOne
    private Heladera heladera;
}
