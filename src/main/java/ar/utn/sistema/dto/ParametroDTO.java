package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ParametroDTO {
    private Integer id;
    private BigDecimal valor;
}
