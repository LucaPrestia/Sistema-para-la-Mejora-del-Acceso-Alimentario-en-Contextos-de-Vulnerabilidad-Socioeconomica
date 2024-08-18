package ar.utn.sistema.entities.usuarios.requisitosContrasena;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public abstract class  Requisitos {
    public abstract boolean evaluarContrasena(String contra);
}
