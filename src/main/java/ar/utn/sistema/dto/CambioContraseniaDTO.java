package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CambioContraseniaDTO {
    private String passwordVieja;
    private String password;
    private String confirmPassword;
}
