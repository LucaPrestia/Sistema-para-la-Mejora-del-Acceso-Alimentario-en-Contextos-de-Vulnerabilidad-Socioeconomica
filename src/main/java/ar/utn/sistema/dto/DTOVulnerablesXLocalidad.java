package ar.utn.sistema.dto;

import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter@NoArgsConstructor
public class DTOVulnerablesXLocalidad {
    private int personaVulnerables ;
    private String localidad;
}
