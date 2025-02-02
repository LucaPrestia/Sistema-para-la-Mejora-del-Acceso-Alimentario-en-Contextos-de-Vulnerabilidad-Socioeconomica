package ar.utn.sistema.dto;

import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import ar.utn.sistema.entities.configuracion.ParametrosGenerales;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ConfigSistemaDTO {
    private List<String> colaboracionesHumano;
    private List<String> colaboracionesJuridico;
    private List<CoeficienteDTO> coeficientes;
    private List<ParametroDTO> parametros;
}

