package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.RegisterDto;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioSesionService servicio;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDto registerDTO, Model model){
        if(servicio.registrarUsuario(registerDTO.getUsername(), registerDTO.getPassword(), registerDTO.getTipoColaborador()) != null) {
            System.out.println(registerDTO.getPassword());
            return "redirect:/login?success";
        } else
            return "redirect:/register?errorContrasenia=true";
    }
}
