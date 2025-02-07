package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.colaboracion.ColaboracionGestionHeladera;
import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.HeladeraRepository;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import ar.utn.sistema.services.ColaboracionService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reporte")

public class ReporteController {



    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private IncidenteRepository incidenteRepository;

    private Colaborador obtenerColaborador(){
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        return colaborador;
    }
    @PostMapping("/incidenteAlta")
    public String buscarViandasDeHeladera(@RequestParam("heladeraId") int idHeladera, @RequestParam("descripcion") String descripcion,@RequestParam("foto") MultipartFile imagen, Model model) {
        try {
            Heladera heladera = heladeraRepository.findById(idHeladera).get();
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            Colaborador colaborador = obtenerColaborador();
            IncidenteFallaTecnica incidenteFallaTecnica = new IncidenteFallaTecnica(LocalDateTime.now(),heladera,colaborador,descripcion,imagen.getBytes(),colaborador.getDireccion().getLocalidad());
            incidenteRepository.save(incidenteFallaTecnica);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }



}
