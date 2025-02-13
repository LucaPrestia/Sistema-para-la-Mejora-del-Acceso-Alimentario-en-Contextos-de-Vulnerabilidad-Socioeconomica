package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.colaboracion.RubroServicio;
import ar.utn.sistema.entities.colaboracion.TipoFrecuencia;
import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ParametrosGeneralesRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.repositories.reportes.ReporteFallasHeladeraRepository;
import ar.utn.sistema.repositories.reportes.ReporteViandasColaboradorRepository;
import ar.utn.sistema.repositories.reportes.ReporteViandasHeladeraRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vistas")
public class VistasController {

    // todo: en todas las vistas, por seguridad, tendríamos que verificar el rol del usuario logueado!!!
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
    @Autowired
    private ParametrosGeneralesRepository parametrosGeneralesRepository;
    @Autowired
    private ReporteViandasColaboradorRepository rReporteViandasColaborador;
    @Autowired
    private ReporteFallasHeladeraRepository rReporteFallasHeladera;
    @Autowired
    private ReporteViandasHeladeraRepository rReporteViandasHeladera;

    @Autowired
    private IncidenteRepository incidenteRepository;

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
    @GetMapping("/GESTION_HELADERA")
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
    @GetMapping("/DINERO")
    public String cargarPaginaHacerDonacionDinero(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<TipoFrecuencia> frecuenciaList = List.of(TipoFrecuencia.values());
        model.addAttribute("frecuenciaList", frecuenciaList);

        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: donacionDinero";

    }
    @GetMapping("/DONACION_VIANDAS")
    public String cargarPaginaDonacionVianda(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException {
        Optional<Colaborador> colaborador = colaboradorRepository.findById(sesion.obtenerUsuarioAutenticado().getUsuario().getId());
        if (colaborador.isPresent()) {
            if (colaborador.get().getDireccion() == null) {
                model.addAttribute("errorMessage", "Debe tener cargada una dirección, donde recibirá una tarjeta para poder abrir heladeras y realizar colaboraciones de viandas. Por favor, dirigirse a 'Mi Perfil' y actualizar los datos");
                return "fragments/error :: errorDiv";
            }
        } else {
            model.addAttribute("errorMessage", "Colaborador no encontrado");
            return "fragments/error :: errorDiv";
        }

        List<Heladera> heladeras = heladeraRepository.findByEstado(EstadoHeladera.ACTIVA);
        model.addAttribute("heladeraList", heladeras);

        return "fragments/colaboraciones :: donacionVianda";
    }
    @GetMapping("/ENTREGA_TARJETAS")
    public String agregarPersonaVulnerable(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException {
        Optional<Colaborador> colaborador = colaboradorRepository.findById(sesion.obtenerUsuarioAutenticado().getUsuario().getId());
        if (colaborador.isPresent()) {
            if (colaborador.get().getDireccion() == null) {
                model.addAttribute("errorMessage", "Debe tener cargada una dirección para poder recibir las tarjetas a repartir. Por favor, dirigirse a Mi Perfil y actualizar los datos");
                return "fragments/error :: errorDiv";
            }
        } else {
            model.addAttribute("errorMessage", "Colaborador no encontrado");
            return "fragments/error :: errorDiv";
        }
        return "fragments/colaboraciones :: agregarPersonaVulnerable";
    }
    @GetMapping("/OFERTA_SERVICIO")
    public String cargarPaginaOfrecerServicio(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        List<RubroServicio> rubroServicios = Arrays.stream(RubroServicio.values()).toList();
        model.addAttribute("rubroServicioList", rubroServicios);


        if (success != null && success) {
            model.addAttribute("success", true);
        }
        return "fragments/colaboraciones :: ofrecerServicio";
    }
    @GetMapping("/REDISTRIBUCION_VIANDAS")
    public String cargaDistribuirVianda(Model model){
        Optional<Colaborador> colaborador = colaboradorRepository.findById(sesion.obtenerUsuarioAutenticado().getUsuario().getId());
        if (colaborador.isPresent()) {
            if (colaborador.get().getDireccion() == null) {
                model.addAttribute("errorMessage", "Debe tener cargada una dirección, donde recibirá una tarjeta para poder abrir heladeras y realizar colaboraciones de viandas. Por favor, dirigirse a 'Mi Perfil' y actualizar los datos");
                return "fragments/error :: errorDiv";
            }
        } else {
            model.addAttribute("errorMessage", "Colaborador no encontrado");
            return "fragments/error :: errorDiv";
        }
        List<Heladera> heladeras = heladeraRepository.findAll();
        List<Heladera> heladerasActivas = heladeraRepository.findByEstado(EstadoHeladera.ACTIVA);
        model.addAttribute("heladeras", heladeras);
        model.addAttribute("heladerasActivas", heladerasActivas);
        return "fragments/colaboraciones :: distribuirVianda";
    }
    @GetMapping("/canjearPuntos")
    public String cargarcanjearPuntos(@RequestParam(value = "success", required = false) Boolean success, Model model) throws IOException
    {
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).get();
        DecimalFormat df = new DecimalFormat("#.##");

        if (colaborador != null) {
            model.addAttribute("puntosDisponibles",  df.format(colaborador.getPuntosDisponibles()));
            model.addAttribute("puntosDisponiblesDouble", colaborador.getPuntosDisponibles());
            List<OfertaCanje> ofertas = ofertaCanjeRepository.findAll();
            model.addAttribute("ofertasCanje", ofertas);

            if (success != null && success) {
                model.addAttribute("success", true);
            }
            return "fragments/canjePuntos :: canjearPuntos";
        } else {
            model.addAttribute("errorMessage", "Colaborador no encontrado");
            return "redirect:/home?error=true";
        }


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
        model.addAttribute("colaboraciones", tipoColaboracionRepository.findAll());
        model.addAttribute("colaboracionesHumano", tipoColaboracionRepository.findByTipoColaborador("PERSONA_HUMANA"));
        model.addAttribute("colaboracionesJuridico", tipoColaboracionRepository.findByTipoColaborador("PERSONA_JURIDICA"));
        model.addAttribute("action", "admin/configSistema");
        model.addAttribute("parametros", parametrosGeneralesRepository.findAll());
        model.addAttribute("coeficientes", coeficientesColaboracionRepository.findAll());
        return "fragments/administrador :: configSistema";
    }

    @GetMapping("/cargaMasiva")
    public String cargaCargaMasiva(Model model){
        return "fragments/administrador :: cargaMasiva";
    }

    @GetMapping("/reportes")
    public String cargaReportes(Model model){
        model.addAttribute("reporteFallas", rReporteFallasHeladera.findAll());
        model.addAttribute("reporteViandasColaborador", rReporteViandasColaborador.findAll());
        model.addAttribute("reporteViandasHeladera", rReporteViandasHeladera.findAll());
        return "fragments/administrador :: reportes";
    }

    @GetMapping("/registrarTecnico")
    public String cargaRegistroTecnico(Model model){
        model.addAttribute("tipoDocumento", TipoDocumento.values());
        model.addAttribute("tipoContacto", MedioNotificacion.values());
        return "fragments/administrador :: registrarTecnico";
    }

    @GetMapping("/reportesIncidentesVer")
    public String cargaReportesIncidentesVer(Model model){
        Integer usuario = sesion.obtenerUsuarioAutenticado().getUsuario().getId();
        String rol = sesion.obtenerUsuarioAutenticado().getRol();

        List<IncidenteFallaTecnica> incidentesFallas;

        switch (rol) {
            case "COLABORADOR_FISICO":
                incidentesFallas = incidenteRepository.findFallasBySuscriptor(usuario);
                break;

            case "COLABORADOR_JURIDICO":
                Colaborador colaborador = colaboradorRepository.findById(usuario).orElse(null);
                Heladera heladera = (colaborador != null) ? colaborador.getHeladera() : null;
                incidentesFallas = (heladera != null)
                        ? incidenteRepository.findFallasByHeladera(heladera)
                        : Collections.emptyList();
                break;

            case "ADMIN":
                incidentesFallas = incidenteRepository.findAllFallasTecnicas();
                break;

            default:
                incidentesFallas = Collections.emptyList();
        }

        model.addAttribute("incidentesFallas", incidentesFallas);
        return "fragments/reportes :: reportesIncidentesVer";
    }

    @GetMapping("/reportesIncidentesReportar")
    public String cargaReportesIncidentesReportar(Model model){
        model.addAttribute("heladeraList", heladeraRepository.findByEstado(EstadoHeladera.ACTIVA));
        return "fragments/reportes :: reportesIncidentesReportar";
    }

    @GetMapping("/reportesAlerta")
    public String menuReportesAlertas(Model model){
        Integer usuario = sesion.obtenerUsuarioAutenticado().getUsuario().getId();
        String rol = sesion.obtenerUsuarioAutenticado().getRol();

        List<IncidenteAlerta> incidenteAlertas;

        switch (rol) {
            case "COLABORADOR_FISICO":
                incidenteAlertas = incidenteRepository.findAlertasBySuscriptor(usuario);
                break;

            case "COLABORADOR_JURIDICO":
                Colaborador colaborador = colaboradorRepository.findById(usuario).orElse(null);
                Heladera heladera = (colaborador != null) ? colaborador.getHeladera() : null;
                incidenteAlertas = (heladera != null)
                        ? incidenteRepository.findAlertaByHeladera(heladera)
                        : Collections.emptyList();
                break;

            case "ADMIN":
                incidenteAlertas = incidenteRepository.findAllAlertas();
                break;

            default:
                incidenteAlertas = Collections.emptyList();
        }

        model.addAttribute("incidentesAlerta", incidenteAlertas);
        return "fragments/reportes :: reportesAlerta";
    }
    @GetMapping("/suscripcion")
    public String cargaSuscripcion(Model model){
        model.addAttribute("heladeras", heladeraRepository.findAll());
        model.addAttribute("preferenciasNoficacion", PreferenciaNotificacion.values());
        return "fragments/vistas :: suscripcion";
    }
    @GetMapping("/mapa")
    public String energiaModelo(Model model){
        double lat = -34.6597832;
        double lon = -58.4680729;
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "fragments/map";
    }
}
