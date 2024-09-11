package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.usuarios.requisitosContrasena.TOP10000;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Tamanio;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Usuario {
    private Rol rol;
    private String usuario;
    private String contrasena;
    private List<Requisitos> requisitos ;
    public boolean registrarUsuario(String user,String pass ){
        if(this.requisitos.stream().allMatch(x-> x.evaluarContrasena(pass))){
            this.usuario = user;
            this.contrasena = contrasena;
            return true;
            //TODO guardar usuario en la base de datos
        }
        return false;
    }
    public  Usuario(){
        this.requisitos = new ArrayList<>();
        this.requisitos.add(new Tamanio());
        this.requisitos.add(new TOP10000());
    }
}
