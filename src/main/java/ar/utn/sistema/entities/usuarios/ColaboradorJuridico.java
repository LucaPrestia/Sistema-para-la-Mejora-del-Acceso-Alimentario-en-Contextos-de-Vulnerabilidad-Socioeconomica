package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
@DiscriminatorValue("juridico")
public class ColaboradorJuridico extends Colaborador {
    private String razonSocial;
    private String rubro;
    private String cuit;

    @Enumerated(EnumType.STRING)
    private TipoJuridico tipoJuridico;

    public ColaboradorJuridico(String razonSocial, String rubro, String cuit, TipoJuridico tipoJuridico) {
        this.razonSocial = razonSocial;
        this.rubro = rubro;
        this.cuit = cuit;
        this.tipoJuridico = tipoJuridico;
    }
    public ColaboradorJuridico(Usuario usuario){
        this.setUsuario(usuario);
    }

    @Override
    public String getNombreCompleto() {
        return razonSocial + " (" + tipoJuridico.getValue() + ")";
    }
}
