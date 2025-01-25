package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.RegisterDto;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.ColaboracionDinero;
import ar.utn.sistema.entities.colaboracion.ColaboracionGestionHeladera;
import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.colaboracion.TipoFrecuencia;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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

    @GetMapping("/colocarHeladera")
    public String cargarPaginaColocarHeladera(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<Direccion> direccionesSugeridas = ServicioDeUbicacionHeladera.instancia().listadoPosicionesHeladera(new Coordenadas(), 2);
        direccionesSugeridas = direccionRepository.saveAll(direccionesSugeridas);
        model.addAttribute("direccionesLista", direccionesSugeridas);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "colaboraciones/colocarHeladera";
    }

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
            //crea la colaboracion y saca el coeficiente correspondiente por sql
            Double coeficientePuntos = coeficientesColaboracionRepository.findById(1).get().getCoeficientePuntos(); //pongo un coef cualquiera
          //  Double coeficientePuntos = coeficientesColaboracionRepository.findByTipoColaboracion(TipoColaboracionEnum.GESTION_HELADERA).get(0).getCoeficientePuntos();
            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(nuevaHeladera,coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            return "redirect:/colaboracion/colocarHeladera?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar la heladera: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/hacerseCargoHeladera")
    public String cargarPaginaHacerseCargoHeladera(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<Heladera> heladerasSinOwner = heladeraRepository.findByOwnerIsNull();
        model.addAttribute("heladeraList", heladerasSinOwner);


        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "colaboraciones/hacerseCargoHeladera";
    }

    @PostMapping("/hacerseCargoHeladera")
    public String guardarHacerseCargoHeladera(@RequestParam("direccionSeleccionada") Integer heladeraId, Model model) {
        try {
            Heladera heladera = heladeraRepository.findById(heladeraId).get();
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            System.out.println(owner.getId());
            heladera.setOwner(owner);
            heladeraRepository.save(heladera);
           // Double coeficientePuntos = coeficientesColaboracionRepository.findByTipoColaboracion(TipoColaboracionEnum.GESTION_HELADERA).get(0).getCoeficientePuntos();
            Double coeficientePuntos = coeficientesColaboracionRepository.findById(1).get().getCoeficientePuntos(); //pongo un coef cualquiera

            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(heladera, coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            return "redirect:/colaboracion/hacerseCargoHeladera?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar la heladera: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/donacionDinero")
    public String cargarPaginaHacerDonacionDinero(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<TipoFrecuencia> frecuenciaList = List.of(TipoFrecuencia.values());
        model.addAttribute("frecuenciaList", frecuenciaList);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "colaboraciones/donacionDinero";
    }

    @PostMapping("/donacionDinero")
    public String guardarHacerDonacionDinero(@RequestParam("dinero") double dinero,@RequestParam("frecuenciaSeleccionada") String id_frecuencia, Model model) {
       try {

            //Double coeficientePuntos = coeficientesColaboracionRepository.findByTipoColaboracion(TipoColaboracionEnum.DINERO).get(0).getCoeficientePuntos();
            Double coeficientePuntos = coeficientesColaboracionRepository.findById(1).get().getCoeficientePuntos(); //pongo un coef cualquiera
            System.out.println(coeficientePuntos);
            ColaboracionDinero colaboracionDinero = new ColaboracionDinero((float) dinero,TipoFrecuencia.valueOf(id_frecuencia),coeficientePuntos);
            System.out.println(colaboracionDinero);
            colaboracionRepository.save(colaboracionDinero);
               return "redirect:/colaboracion/donacionDinero?success=true";
        } catch (Exception e) {
            System.out.println(e.getMessage());
           return "error";
        }
    }
}
