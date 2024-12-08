package ar.utn.sistema.entities.usuarios.requisitosContrasena;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public abstract class  Requisitos {
    private String mensajeError;
    public abstract boolean evaluarContrasena(String contra);

    public Requisitos(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
