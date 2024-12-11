package ar.utn.sistema.model;

import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ColaboradorColaboracionConfig {
    private List<ColaboradorColaboracion> formColaboracion;
}
