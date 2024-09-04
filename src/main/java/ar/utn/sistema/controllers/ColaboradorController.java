package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.model.FormulariosConfig;
import ar.utn.sistema.services.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/colaborador")
public class ColaboradorController {
    @Autowired
    private FormularioService service;

    // Formulario de alta para el colaborador, filtrando por tipo (acá ya registró su usuario y se le preguntó por su tipo de colaborador)
    @GetMapping("/alta/{tipoColaborador}")
    public String obtenerFormulario(@PathVariable int tipoColaborador, Model model){
        // 0: persona humana; 1: persona juridica
        String tipo = tipoColaborador == 0 ? "PERSONA_HUMANA" : "PERSONA_JURIDICA";
        Formulario formulario = service.obtenerFormularioPorTipo(tipo);
        model.addAttribute("formularioColaborador", formulario);
        model.addAttribute("tipoColaborador", tipoColaborador);
        return "formularioColaborador"; // esto carga los datos en el formularioColaborador.hbs
    }

    @PostMapping("/submitAltaColaborador")
    public String procesarFormulario(@RequestParam int tipoColaborador, @RequestParam Map<String, String> formData) {
        if (tipoColaborador == 0) {
            ColaboradorFisico colaborador = new ColaboradorFisico();
            formData.forEach((key, value) -> {
                // TODO: Asignar valores a los campos de ColaboradorHumano
                // TODO: Esto depende de la estructura de ColaboradorHumano y cómo se mapean los campos
            });
            // Persistir colaborador en la base de datos!
        } else {
            ColaboradorJuridico colaborador = new ColaboradorJuridico();
            // Llenar datos en colaborador
            formData.forEach((key, value) -> {
                // TODO: Asignar valores a los campos de ColaboradorJuridico
                // TODO: Esto depende de la estructura de ColaboradorJuridico y cómo se mapean los campos
            });
            // Persistir colaborador en la base de datos!
        }
        return "redirect:/colaborador/confirmacion"; // Redirigir a una página de confirmación o éxito
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        return "confirmacionColaborador"; // TODO: AGREGAR HBS
    }

}
