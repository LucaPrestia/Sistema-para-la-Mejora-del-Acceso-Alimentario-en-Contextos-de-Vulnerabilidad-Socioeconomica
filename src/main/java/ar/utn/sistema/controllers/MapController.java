package ar.utn.sistema.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MapController {

    private static Logger LOG = LoggerFactory.getLogger(MapController.class);

    @GetMapping("/map") // por defecto puse la lat y lon de la utn
    public String showMap(@RequestParam(defaultValue = "-34.6597832") double lat,
                          @RequestParam(defaultValue = "-58.4680729") double lon,
                          Model model) {
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "map";
    }

    // esto lo dejé acá como ejemplo nomas
    @GetMapping("/index")
    public String showIndex(Model model) {
        model.addAttribute("title", "Página de Inicio");
        model.addAttribute("description", "Bienvenido a la página de inicio de nuestra aplicación.");
        LOG.info("Estas por acceder a index");
        return "index";
    }
}
