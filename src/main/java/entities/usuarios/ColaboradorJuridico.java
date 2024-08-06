package entities.usuarios;

import entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colaborador_juridico")
@Getter @Setter @NoArgsConstructor
public class ColaboradorJuridico {
    @Column(name = "razon_social")
    private String razonSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_juridico")
    private TipoJuridico tipoJuridico; // TODO: si se agregan más tipos jurídicos sería conveniente que esto sea una tabla y no un enum

    @OneToOne
    @JoinColumn(name = "heladera_id",  nullable = true) // un colaborador jurídico puede no aportar heladera => posible NULL
    private Heladera heladera;

}
