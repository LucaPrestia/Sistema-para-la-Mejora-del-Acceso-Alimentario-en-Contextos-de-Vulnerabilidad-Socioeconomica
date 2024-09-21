package ar.utn.sistema;

import ar.utn.sistema.SistemaAccesoAlimentario;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Tamanio;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.TOP10000;
import ar.utn.sistema.entities.usuarios.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
   /* @Test COMENTADO PARA NO GASTAR USOS DE LA API QUEDAN 990 USOS
    public void obtenerCoordenadasHeladera() throws IOException {
        ServicioDeUbicacionHeladera servicio = ServicioDeUbicacionHeladera.instancia();
        Coordenadas centro = new Coordenadas(123.1,93.12);
        double radio = 12.2;
        List<Coordenadas> coordenadas = servicio.listadoPosicionesHeladera(centro, radio);
        System.out.println(coordenadas.stream().map(Coordenadas::getLatitud).toList());
        assertNotNull(coordenadas);
    }*/
}
