package ar.utn.sistema.controllers;

import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UsuarioSesionService sesion;

    @GetMapping("/home")
    public String loadHome(Model model){
        model.addAttribute("username", sesion.obtenerUsuarioAutenticado().getUsername());
        double lat = -34.6597832;
        double lon = -58.4680729;
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "home";
    }
}
