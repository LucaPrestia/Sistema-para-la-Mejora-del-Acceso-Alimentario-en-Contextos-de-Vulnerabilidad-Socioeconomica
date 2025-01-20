package ar.utn.sistema.entities.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class Ong extends Rol {
    private String nombre;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Ong(String nombre, Usuario usuario){
        this.nombre = nombre;
        this.usuario = usuario;
    }
}
