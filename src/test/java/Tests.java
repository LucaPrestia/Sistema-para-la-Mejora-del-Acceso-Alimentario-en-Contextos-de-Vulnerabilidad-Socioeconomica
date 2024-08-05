
import entities.usuarios.requisitosContrasena.Requisitos;
import entities.usuarios.requisitosContrasena.Tamanio;
import entities.usuarios.requisitosContrasena.TOP10000;
import entities.usuarios.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;


public class Tests {


    Tamanio tam = new Tamanio();
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
    @Test
    public void noRegistrarUsuarioTop(){
        Usuario usuarioNuevo = new Usuario();
        boolean resp = usuarioNuevo.registrarUsuario("usuario","123456789");
        Assert.assertFalse(resp);
    }
    @Test
    public void noRegistrarUsuarioTamanio(){
        Usuario usuarioNuevo = new Usuario();
        boolean resp = usuarioNuevo.registrarUsuario("usuario","155");
        Assert.assertFalse(resp);
    }
}
