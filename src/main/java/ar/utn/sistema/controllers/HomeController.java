package ar.utn.sistema.controllers;

import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UsuarioSesionService sesion;

    @GetMapping("/home")
    public String loadHome(@RequestParam(value = "success", required = false)Boolean succes,@RequestParam(value = "error", required = false) Boolean error ,Model model){
        model.addAttribute("username", sesion.obtenerUsuarioAutenticado().getUsername());
        model.addAttribute("rol", sesion.obtenerUsuarioAutenticado().getRol());
        model.addAttribute("success", succes);
        model.addAttribute("error", error);
        System.out.println(sesion.obtenerUsuarioAutenticado().getRol());
        double lat = -34.6597832;
        double lon = -58.4680729;
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "home";
    }
}
