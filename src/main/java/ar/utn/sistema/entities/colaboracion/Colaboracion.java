package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
public abstract class Colaboracion extends PersistenciaID {
    private Colaborador colaborador;
    private Date fechaColaboracion;
}
