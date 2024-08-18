package ar.utn.sistema;

import ar.utn.sistema.SistemaAccesoAlimentario;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Tamanio;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.TOP10000;
import ar.utn.sistema.entities.usuarios.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SistemaAccesoAlimentario.class)
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
        assertTrue(resp);
    }
    @Test
    public void noRegistrarUsuarioTop(){
        Usuario usuarioNuevo = new Usuario();
        boolean resp = usuarioNuevo.registrarUsuario("usuario","123456789");
        assertFalse(resp);
    }
    @Test
    public void noRegistrarUsuarioTamanio(){
        Usuario usuarioNuevo = new Usuario();
        boolean resp = usuarioNuevo.registrarUsuario("usuario","155");
        assertFalse(resp);
    }
}
