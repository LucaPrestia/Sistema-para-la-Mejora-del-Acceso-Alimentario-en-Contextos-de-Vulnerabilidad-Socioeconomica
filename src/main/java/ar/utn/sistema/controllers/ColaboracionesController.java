package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DonacionViandaDTO;
import ar.utn.sistema.dto.RegisterDto;
import ar.utn.sistema.dto.ViandaDTO;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.*;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.CoeficientesColaboracionService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.hibernate.event.internal.EvictVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller()
@RequestMapping("/colaboracion")
public class ColaboracionesController {


    @Autowired
    private ColaboracionRepository colaboracionRepository;

    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private CoordenadasRepository coordenadasRepository;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CoeficientesColaboracionRepository coeficientesColaboracionRepository;
    @Autowired
    private TipoColaboracionRepository tipoColaboracionRepository;
    @Autowired
    private ViandaRepository viandaRepository;
    @Autowired
    private CoeficientesColaboracionService coeficientesColaboracionService;

    @PostMapping("/colocarHeladera")
    public String guardarHeladeraColocar(
            @RequestParam("nombre") String nombre,
            @RequestParam("tempMin") double tempMin,
            @RequestParam("tempMax") double tempMax,
            @RequestParam("maxViandas") int maxViandas,
            @RequestParam("direccionSeleccionada") Integer direccionId,
            Model model) {
        try {
            Direccion direccion = direccionRepository.findById(direccionId).get();
            System.out.println(direccion.getCalle());
            System.out.println(direccion.getId());
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            System.out.println(owner.getId());
            Heladera nuevaHeladera = new Heladera(nombre, owner, direccion, tempMax, tempMin, maxViandas);
            heladeraRepository.save(nuevaHeladera);
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.GESTION_HELADERA.name());
            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(nuevaHeladera,coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }


    @PostMapping("/hacerseCargoHeladera")
    public String guardarHacerseCargoHeladera(@RequestParam("direccionSeleccionada") Integer heladeraId, Model model) {
        try {
            Heladera heladera = heladeraRepository.findById(heladeraId).get();
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            System.out.println(owner.getId());
            heladera.setOwner(owner);
            heladeraRepository.save(heladera);
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.GESTION_HELADERA.name());
            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(heladera, coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }

    @PostMapping("/donacionDinero")
    public String guardarHacerDonacionDinero(@RequestParam("dinero") double dinero,@RequestParam("frecuenciaSeleccionada") String id_frecuencia, Model model) {
        try {

            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.DINERO.name());
            System.out.println(coeficientePuntos);
            ColaboracionDinero colaboracionDinero = new ColaboracionDinero((float) dinero, TipoFrecuencia.valueOf(id_frecuencia), coeficientePuntos);
            System.out.println(colaboracionDinero);
            colaboracionRepository.save(colaboracionDinero);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }
    @PostMapping("/donacionVianda")
    public String recibirDonacion(@RequestBody DonacionViandaDTO donacion, Model model) {
        System.out.println("Heladera Seleccionada: " + donacion.getHeladeraSeleccionada());
        for (ViandaDTO vianda : donacion.getViandas()) {
            System.out.println("Comida: " + vianda.getComida());
            System.out.println("Fecha Caducidad: " + vianda.getFechaCaducidad());
            System.out.println("Calorías: " + vianda.getCalorias());
            System.out.println("Peso: " + vianda.getPeso());
        }
        try {
            // Lógica de procesamiento como la que ya tienes
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.DONACION_VIANDAS.name());
            System.out.println(coeficientePuntos);

            List<Vianda> viandasNuevas = new ArrayList<>();
            for (ViandaDTO vianda : donacion.getViandas()) {
                Heladera heladera = heladeraRepository.findById(donacion.getHeladeraSeleccionada()).get();
                Vianda nuevaVianda = new Vianda(vianda.getComida(), vianda.getFechaCaducidad(), heladera, vianda.getCalorias(), vianda.getPeso());
                viandasNuevas.add(nuevaVianda);
            }
            viandaRepository.saveAll(viandasNuevas);
            ColaboracionVianda colaboracionVianda = new ColaboracionVianda(viandasNuevas, viandasNuevas.size(), coeficientePuntos);
            System.out.println(colaboracionVianda);
            colaboracionRepository.save(colaboracionVianda);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }
}
