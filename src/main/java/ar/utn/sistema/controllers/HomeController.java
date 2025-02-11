package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.CambioContraseniaDTO;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UsuarioSesionService sesion;

    @Autowired
    private TipoColaboracionRepository rTipoColaboracion;

    @Autowired
    private ColaboradorRepository rColaborador;

    @GetMapping("/home")
    public String loadHome(@RequestParam(value = "success", required = false)Boolean succes,@RequestParam(value = "error", required = false) Boolean error ,Model model){
        String rol = sesion.obtenerUsuarioAutenticado().getRol();
        model.addAttribute("username", sesion.obtenerUsuarioAutenticado().getUsername());
        model.addAttribute("rol", rol);
        model.addAttribute("success", succes);
        model.addAttribute("error", error);

        List<TipoColaboracion> colaboracionesHabilitadas = new ArrayList<>();
        if ("COLABORADOR_FISICO".equals(rol)) {
            colaboracionesHabilitadas = rTipoColaboracion.findByTipoColaborador("PERSONA_HUMANA");
        } else if ("COLABORADOR_JURIDICO".equals(rol)) {
            colaboracionesHabilitadas = rTipoColaboracion.findByTipoColaborador("PERSONA_JURIDICA");
        }

        boolean tieneGestionHeladera = colaboracionesHabilitadas.stream()
                .anyMatch(colaboracion -> "GESTION_HELADERA".equals(colaboracion.getCodigo()));

        model.addAttribute("colaboracionesHabilitadas", colaboracionesHabilitadas);
        model.addAttribute("tieneGestionHeladera", tieneGestionHeladera);

        double lat = -34.6597832;
        double lon = -58.4680729;
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "home";
    }

    @GetMapping("/onboarding")
    public String loadOnBoarding(@RequestParam(value = "success", required = false)Boolean succes,@RequestParam(value = "error", required = false) Boolean error ,Model model){
        // depende de su rol!!
        UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
        model.addAttribute("rol", usuario.getRol());
        model.addAttribute("tipoContacto", MedioNotificacion.values());
        switch (usuario.getRol()) {
            case "COLABORADOR_FISICO":
                model.addAttribute("tipoDocumento", TipoDocumento.values());
                model.addAttribute("colaboracionesHabilitadas", rTipoColaboracion.findByTipoColaborador("PERSONA_HUMANA"));
                model.addAttribute("action", "colaborador/configuracion/humano");

                // pueden haber algunos datos ya ingresados por tratarse de un nuevo usuario registrado con carga masiva:
                ColaboradorFisico colaborador = (ColaboradorFisico) usuario.getUsuario();
                if(colaborador.getDocumento() != null) if( !colaborador.getDocumento().isEmpty()){
                   // no le paso el colaborador que obtuve de la sesión porque ese no tiene cargado los campos con LAZY EVALUATION!!
                   ColaboradorFisico colaboradorCompleto = (ColaboradorFisico) rColaborador.findById(colaborador.getId()).get();
                   model.addAttribute("colaborador", colaboradorCompleto);
                }
                break;
            case "COLABORADOR_JURIDICO":
                model.addAttribute("tipoJuridico", TipoJuridico.values());
                model.addAttribute("colaboracionesHabilitadas", rTipoColaboracion.findByTipoColaborador("PERSONA_JURIDICA"));
                model.addAttribute("action", "colaborador/configuracion/juridico");
                model.addAttribute("colaborador", (ColaboradorJuridico) rColaborador.findById(usuario.getUsuario().getId()).get());
                break;
            default: break;
        }
        return "onboarding";
    }

    @GetMapping("/cambioContrasenia")
    public String cambiarContrasenia(Model model) {
        model.addAttribute("usernameLog", sesion.obtenerUsuarioAutenticado().getUsername());
        model.addAttribute("cambioContraseniaDTO", new CambioContraseniaDTO());
        return "cambioContrasenia";
    }
}
