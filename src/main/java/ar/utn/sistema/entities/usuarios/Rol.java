package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class Rol extends PersistenciaID implements Serializable {
}
