package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.TOP10000;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Tamanio;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter
public class Usuario extends PersistenciaID {
    private String rol; // COLABORADOR_FISICO, COLABORADOR_JURIDICO, TECNICO, ADMIN, PERSONA_VULNERABLE
    private String usuario;
    private String contrasena;

    private int nuevo; // 1: logueado por primera vez => corresponde on bording de carga de datos según su rol; 0: ya entro a la aplicación; 2:corresponde cambiar contrasenia

    @Transient
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
        this.nuevo = 1;
        this.requisitos = new ArrayList<>();
        this.requisitos.add(new Tamanio());
        this.requisitos.add(new TOP10000());
    }

}
