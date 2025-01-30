package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class DonacionViandaDTO {
    private Integer heladeraSeleccionada;
    private List<ViandaDTO> viandas;

    // Getters y Setters
}
