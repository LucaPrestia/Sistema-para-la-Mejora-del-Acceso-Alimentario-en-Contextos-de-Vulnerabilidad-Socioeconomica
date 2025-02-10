package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.CambioContraseniaDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioSesionService servicio;

    @Autowired
    private UsuarioSesionService sesion;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDto registerDTO, RedirectAttributes redirectAttributes){
        try {
            servicio.registrarUsuario(registerDTO.getUsername(), registerDTO.getPassword(), registerDTO.getTipoColaborador());
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(@ModelAttribute("cambioContraseniaDTO") CambioContraseniaDTO cambioDTO, RedirectAttributes redirectAttributes){
        try {
            System.out.println("pass:" + cambioDTO.getPasswordVieja());
            servicio.cambiarContrasenia(sesion.obtenerUsuarioAutenticado().getUsername(), cambioDTO.getPasswordVieja(), cambioDTO.getPassword());
            return "redirect:/onboarding";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
            return "redirect:/cambioContrasenia";
        }
    }
}
