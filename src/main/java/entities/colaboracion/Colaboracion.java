package entities.colaboracion;

import entities.PersistenciaID;
import entities.usuarios.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
public abstract class Colaboracion extends PersistenciaID {
    private Colaborador colaborador;
    private Date fechaColaboracion;
}
