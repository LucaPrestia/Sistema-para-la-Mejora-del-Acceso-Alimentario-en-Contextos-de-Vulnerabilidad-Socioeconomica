package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.colaboracion.ColaboracionGestionHeladera;
import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Tecnico;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.services.ColaboracionService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private TecnicoRepository tecnicoRepository;

    private Colaborador obtenerColaborador(){
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        return colaborador;
    }
    @PostMapping("/incidenteAlta")
    @Transactional
    public String buscarViandasDeHeladera(@RequestParam("heladeraId") int idHeladera, @RequestParam("descripcion") String descripcion,@RequestParam("foto") MultipartFile imagen, Model model) {
        try {
            Heladera heladera = heladeraRepository.findById(idHeladera).get();
            Colaborador colaborador = obtenerColaborador();
            IncidenteFallaTecnica incidenteFallaTecnica = new IncidenteFallaTecnica(LocalDateTime.now(),heladera,colaborador,descripcion,imagen.getBytes(),heladera.getDireccion().getLocalidad());
            List<Tecnico> tecnicos = tecnicoRepository.findAllByAreaCoberturaEqualsIgnoreCase(heladera.getDireccion().getLocalidad());
            if(tecnicos==null ){
                tecnicos = tecnicoRepository.findAll();
            } else if(tecnicos.isEmpty()) {
                tecnicos = tecnicoRepository.findAll();
            }
            colaborador.registrarFalla(incidenteFallaTecnica,tecnicos);
            String mensaje = "Un colaborador ha registrado una falla técnica en la heladera de nombre '" +
                    incidenteFallaTecnica.getHeladera().getNombre() + "' ubicada en la dirección " +
                    incidenteFallaTecnica.getHeladera().getDireccion().obtenerCadenaDireccion() + " indicando lo siguiente: " +  incidenteFallaTecnica.getDescripcion();
            List<Colaborador> colaboradores = heladera.getSuscriptores();
            for (Colaborador c : colaboradores) {

                if(c.correspondeVerificar(PreferenciaNotificacion.DESPERFECTO,0)){
                    System.out.println("antes");

                    c.notificar(new Notificacion(mensaje));
                    System.out.println(mensaje);
                }
            }
            incidenteRepository.save(incidenteFallaTecnica);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }



}
