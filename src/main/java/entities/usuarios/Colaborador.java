package entities.usuarios;

import entities.Direccion;
import entities.PersistenciaID;
import entities.colaboracion.Colaboracion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.MedioDeContacto;

import java.util.List;

@Entity
@Table(name = "colaborador")
@Getter @Setter @NoArgsConstructor // crea constructor vacío para que lo tome hibernate más adelante para persistir
@Inheritance(strategy = InheritanceType.JOINED) // con esto crea una tabla para esta clase abstracta y luego una tabla por cada subclase que se extienda de Colaborador
public abstract class Colaborador extends PersistenciaID {

    private List<MedioDeContacto> medioDeContacto;

    @Embedded // para que persista los datos de la clase dirección en la tabla colaborador
    private Direccion direccion;

    @OneToMany(mappedBy = "colaborador") // RELACIÓN BIODIRECCIONAL!! -> SE PERSISTE LA RELACIÓN EN TABLA DEL LADO DE LA COLABORACION (tabla hija de Colaborador)
    private List<Colaboracion> colaboraciones;

    public void agregarColaboracion(Colaboracion colaboracion){
        this.colaboraciones.add(colaboracion);
    }

}
