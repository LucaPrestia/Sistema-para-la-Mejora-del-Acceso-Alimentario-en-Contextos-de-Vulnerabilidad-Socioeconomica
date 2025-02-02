package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.HeladeraRepository;
import ar.utn.sistema.repositories.OfertaCanjeRepository;
import ar.utn.sistema.repositories.ViandaRepository;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping()
public class ViandaController {



    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    private Colaborador obtenerColaborador(){
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        return colaborador;
    }
    @GetMapping("/vianda")
    @ResponseBody
    public List<Vianda> buscarViandasDeHeladera(@RequestParam("heladeraId") int idHeladera) {
        Heladera heladera = heladeraRepository.findById(idHeladera).orElse(null);
        List<Vianda> viandasList;
        if (heladera == null) {
            viandasList = new ArrayList<>();
            viandasList.add(new Vianda("No hay viandas en esta heladera"));
        } else {
            viandasList = heladera.getViandas();
        }
        // Impresión de los datos para depuración
        viandasList.forEach(vianda -> System.out.println(vianda.getComida()));
        return viandasList;
    }


}
