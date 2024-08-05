
import entities.usuarios.Requisitos;
import entities.usuarios.TAMANIO;
import entities.usuarios.TOP10000;
import entities.usuarios.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Tests {


    TAMANIO tam = new TAMANIO();
    TOP10000 top = new TOP10000();
    List<Requisitos> chequeos = new ArrayList<>();


    @BeforeEach
    public void setUp() {

    }
    @Test
    public void registrarUsuario(){
        Usuario usuarioNuevo = new Usuario();
        boolean resp = usuarioNuevo.registrarUsuario("usuario","d2saDSAAQ&sadasd3d1as1das");
        Assert.assertTrue(resp);
    }
}
