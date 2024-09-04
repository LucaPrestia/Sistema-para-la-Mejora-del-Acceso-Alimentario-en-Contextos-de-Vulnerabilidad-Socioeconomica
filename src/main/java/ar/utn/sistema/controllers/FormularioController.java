package ar.utn.sistema.controllers;

import ar.utn.sistema.model.CampoFormulario;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.model.TipoCampo;
import ar.utn.sistema.services.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/formulario")
public class FormularioController {

    @Autowired
    private FormularioService service;

    @GetMapping("/abm/{tipoColaborador}")
    public String mostrarFormulario(@PathVariable int tipoColaborador, Model model) {
        cargarABMFormulario(tipoColaborador, model);
        return "abmFormulario";
    }

    @PostMapping("/agregarCampo")
    public String agregarCampo(@RequestParam("tipoColaborador") String tipoColaborador,
                               @RequestParam("nombreCampo") String nombreCampo,
                               @RequestParam("etiqueta") String etiqueta,
                               @RequestParam("tipo") TipoCampo tipo,
                               @RequestParam(value = "requerido", required = false) boolean requerido,
                               Model model) {
        CampoFormulario nuevoCampo = new CampoFormulario(nombreCampo, etiqueta, tipo, requerido);
        service.agregarCampo(tipoColaborador, nuevoCampo);
        cargarABMFormulario((tipoColaborador.equals("PERSONA_HUMANA") ? 0 : 1), model);
        return "abmFormulario";
    }

    @PostMapping("/eliminarCampo")
    public String quitarCampo(@RequestParam String tipoColaborador,
                              @RequestParam String nombreCampo,
                              Model model) {
        service.eliminarCampo(tipoColaborador, nombreCampo); // TODO: si el campo está registrado como obligatorio no debería dejarlo borrar
        cargarABMFormulario((tipoColaborador.equals("PERSONA_HUMANA") ? 0 : 1), model);
        return "abmFormulario";
    }

    @PostMapping("/modificarCampo")
    public String modificarCampo(@RequestParam("tipoColaborador") String tipoColaborador,
                                 @RequestParam("nombreCampo") String nombreCampo,
                                 @RequestParam("nuevaEtiqueta") String nuevaEtiqueta,
                                 @RequestParam("nuevoTipo") TipoCampo nuevoTipo,
                                 @RequestParam(value = "nuevoRequerido", required = false) boolean requerido,
                                 Model model) {
        service.modificarCampo(tipoColaborador, nombreCampo, nuevaEtiqueta, nuevoTipo, requerido);
        cargarABMFormulario((tipoColaborador.equals("PERSONA_HUMANA") ? 0 : 1), model);
        return "abmFormulario";
    }

    private void cargarABMFormulario(int tipoColaborador, Model model) {
        String tipo = tipoColaborador == 0 ? "PERSONA_HUMANA" : "PERSONA_JURIDICA";
        Formulario formulario = service.obtenerFormularioPorTipo(tipo);
        List<String> tiposDeCampo = Arrays.stream(TipoCampo.values())
                .map(TipoCampo::getValue)
                .collect(Collectors.toList());
        model.addAttribute("formulario", formulario);
        model.addAttribute("tiposDeCampo", tiposDeCampo);
    }
}
