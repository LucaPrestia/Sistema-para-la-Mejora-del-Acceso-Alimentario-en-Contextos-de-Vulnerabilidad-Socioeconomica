package ar.utn.sistema.entities.usuarios;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class Ong extends Rol {
    private String nombre;
}
