package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.services.CargaColaboracionesMasivas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CargaMasivaController {
    @Autowired
    private CargaColaboracionesMasivas cargaColaboracionesMasivas;



    @PostMapping("/carga-masiva")
    public String cargarArchivo(@RequestParam("archivo") MultipartFile archivo, RedirectAttributes redirectAttributes) {

        if (archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, seleccione un archivo.");
            return "redirect:/home?error=true";
        }

        try {
            List<Colaboracion> colaboraciones = cargaColaboracionesMasivas.cargarColaboracionesMasivamente(archivo);
            redirectAttributes.addFlashAttribute("successMessage", "Se cargaron " + colaboraciones.size() + " colaboraciones exitosamente.");
            return "redirect:/home?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al procesar el archivo: " + e.getMessage());
            return "redirect:/home?error=true";
        }
    }
}
