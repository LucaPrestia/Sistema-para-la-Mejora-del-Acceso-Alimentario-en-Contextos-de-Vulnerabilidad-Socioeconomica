package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.colaboracion.RubroServicio;
import ar.utn.sistema.entities.colaboracion.TipoFrecuencia;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.UsuarioSesionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/vistas")
public class VistasController {
    @Autowired
    private ColaboracionRepository colaboracionRepository;
    @Autowired
    private OfertaCanjeRepository ofertaCanjeRepository;

    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private CoordenadasRepository coordenadasRepository;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CoeficientesColaboracionRepository coeficientesColaboracionRepository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    @Autowired
    private TipoColaboracionRepository tipoColaboracionRepository;

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
    @GetMapping("/colocarHeladera")
    public String vistaColocarHeladera(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException {
        List<Direccion> direccionesSugeridas = ServicioDeUbicacionHeladera.instancia().listadoPosicionesHeladera(new Coordenadas(), 2);
        direccionesSugeridas = direccionRepository.saveAll(direccionesSugeridas);
        model.addAttribute("direccionesLista", direccionesSugeridas);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: colocarHeladera" ;
    }
    @GetMapping("/hacerseCargoHeladera")
    public String cargarPaginaHacerseCargoHeladera(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<Heladera> heladerasSinOwner = heladeraRepository.findByOwnerIsNull();
        model.addAttribute("heladeraList", heladerasSinOwner);


        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: hacerseCargoHeladera";
    }
    @GetMapping("/donacionDinero")
    public String cargarPaginaHacerDonacionDinero(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<TipoFrecuencia> frecuenciaList = List.of(TipoFrecuencia.values());
        model.addAttribute("frecuenciaList", frecuenciaList);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: donacionDinero";

    }
    @GetMapping("/donacionVianda")
    public String cargarPaginaDonacionVianda(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<Heladera> heladeras = heladeraRepository.findAll();
        model.addAttribute("heladeraList", heladeras);


        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: donacionVianda";
    }
    @GetMapping("/agregarPersonaVulnerable")
    public String agregarPersonaVulnerable(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: agregarPersonaVulnerable";
    }
    @GetMapping("/ofrecerServicio")
    public String cargarPaginaOfrecerServicio(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<RubroServicio> rubroServicios = Arrays.stream(RubroServicio.values()).toList();
        model.addAttribute("rubroServicioList", rubroServicios);


        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: ofrecerServicio";
    }
    @GetMapping("/canjearPuntos")
    public String cargarcanjearPuntos(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).get();
        //model.addAttribute("colaborador", );
        DecimalFormat df = new DecimalFormat("#.##");

        if (colaborador != null) {
            model.addAttribute("puntosDisponibles",  df.format(colaborador.getPuntosDisponibles()));
        }

        List<OfertaCanje> ofertas = ofertaCanjeRepository.findAll();
        model.addAttribute("ofertasCanje", ofertas);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/canjePuntos :: canjearPuntos";
    }
    @GetMapping("/miperfil")
    public String cargarPaginaMiPerfil(@RequestParam(value = "success", required = false) Boolean success, Model model){
        UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
        model.addAttribute("rol", usuario.getRol());
        model.addAttribute("tipoContacto", MedioNotificacion.values());
        model.addAttribute("username", usuario.getUsername());
        switch (usuario.getRol()) {
            case "COLABORADOR_FISICO":
                model.addAttribute("tipoDocumento", TipoDocumento.values());
                model.addAttribute("colaboracionesHabilitadas", tipoColaboracionRepository.findByTipoColaborador("PERSONA_HUMANA"));
                model.addAttribute("action", "colaborador/configuracion/humano");
                model.addAttribute("colaborador", (ColaboradorFisico) colaboradorRepository.findById(usuario.getUsuario().getId()).get());
                break;
            case "COLABORADOR_JURIDICO":
                model.addAttribute("tipoJuridico", TipoJuridico.values());
                model.addAttribute("colaboracionesHabilitadas", tipoColaboracionRepository.findByTipoColaborador("PERSONA_JURIDICA"));
                model.addAttribute("action", "colaborador/configuracion/juridico");
                model.addAttribute("colaborador", (ColaboradorJuridico) colaboradorRepository.findById(usuario.getUsuario().getId()).get());
                break;
            default: break;
        }
        return "fragments/perfil :: perfil";
    }

    // Menús administrador:
    @GetMapping("/configSistema")
    public String cargaConfigSistema(Model model){
        // todo:
        return "fragments/administrador :: configSistema";
    }

    @GetMapping("/cargaMasiva")
    public String cargaCargaMasiva(Model model){
        // todo:
        return "fragments/administrador :: cargaMasiva";
    }

    @GetMapping("/reportes")
    public String cargaReportes(Model model){
        // todo:
        return "fragments/administrador :: reportes";
    }
}
