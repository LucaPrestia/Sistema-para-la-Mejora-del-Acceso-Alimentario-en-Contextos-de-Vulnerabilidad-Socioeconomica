package ar.utn.sistema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vistas")
public class VistasController {

    // para las vistas estáticas que no necesitan mucho pasaje de parámetro (una función carga muchas vistas estáticas, por su parámetro opcion que se carga de forma dinámica)
    @GetMapping("/{opcion}")
    public String vistaOpcionEstatica(@PathVariable String opcion, Model model) {
        // se podrían pasar atributos para cargar cada vista (campos de formularios, datos preexistentes, etc)
        model.addAttribute("nombre", "Vista " + opcion);

        return "fragments/vistas :: " + opcion ;
    }

    // para las vistas más personalizadas con mucha configuración (hacer tantas funciones cómo vistas personalizadas)
    @GetMapping("/opcion3")
    public String vistaOpcion3(Model model) {
        // se podrían pasar atributos para cargar cada vista (campos de formularios, datos preexistentes, etc)
        model.addAttribute("nombre", "Vista opcion 3");
        model.addAttribute("info", "Info adicional vista personalizada");

        return "fragments/vistas :: opcion3" ;
    }
}
