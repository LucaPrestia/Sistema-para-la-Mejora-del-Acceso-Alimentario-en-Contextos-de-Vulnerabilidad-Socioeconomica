package ar.utn.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String tipoColaborador;
}
