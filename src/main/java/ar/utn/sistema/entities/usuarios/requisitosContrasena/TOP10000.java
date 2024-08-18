package ar.utn.sistema.entities.usuarios.requisitosContrasena;

import lombok.Getter;
import lombok.Setter;
import ar.utn.sistema.utils.LectorArchivo;

@Getter
@Setter
public class TOP10000 extends Requisitos{
    public String ruta = "top10000_Peores_Contras.txt";
    public boolean evaluarContrasena(String contra) {
        String linea;
        LectorArchivo leer = new LectorArchivo();
        while((linea=leer.traerLinea(ruta))!=null){
            //System.out.println(linea);
            if(contra.equals(linea))return false;}
        return true;
    }
}
