package ar.utn.sistema.dto;

import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DonacionPersonaVulnerableDTO {
    private List<PersonaVulnerableDTO> personaVulnerable;


    public List<PersonaVulnerableDTO> getPersonaVulnerable() {
        return personaVulnerable;
    }

    public void setPersonaVulnerable(List<PersonaVulnerableDTO> personaVulnerable) {
        this.personaVulnerable = personaVulnerable;
    }
}
