package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.TOP10000;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Tamanio;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Usuario extends PersistenciaID {
    private String rol;
    private String usuario;
    private String contrasena;
    private List<Requisitos> requisitos;

    public Usuario(){
        this.requisitos = new ArrayList<>();
        this.requisitos.add(new Tamanio());
        this.requisitos.add(new TOP10000());
    }

    // el registrarUsuario lo movi al service del usuario porque es lógica de negocio
    public  Usuario(String username, String contrasena, String rol){
        this.usuario = username;
        this.contrasena = contrasena;
        this.rol = rol;
        this.requisitos = new ArrayList<>();
        this.requisitos.add(new Tamanio());
        this.requisitos.add(new TOP10000());
    }
}
