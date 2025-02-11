package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.PerfilColaboradorDto;
import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.HeladeraRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.ColaboracionService;
import ar.utn.sistema.services.FormularioService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/colaborador")
public class ColaboradorController {

    private static Logger logger = LoggerFactory.getLogger(ColaboradorController.class);
    @Autowired
    private FormularioService serviceFormulario;
    @Autowired
    private ColaboracionService serviceColaboracion;
    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private TipoColaboracionRepository rTipoColaboracion;
    @Autowired
    private ColaboradorRepository repository;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @PostMapping("/configuracion/humano")
    @Transactional
    public String cargarConfiguracionPersonaHumana(@ModelAttribute PerfilColaboradorDto datos,
                                                   @RequestParam("tiposColaboracionIds") List<Integer> tiposColaboracionIds,
                                                   Model model){
        UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
        if(usuario.getRol().equals("COLABORADOR_FISICO")){
            ColaboradorFisico colaborador = (ColaboradorFisico) repository.findById(usuario.getUsuario().getId()).get();
            List<TipoColaboracion> tiposColaboracion = rTipoColaboracion.findAllById(tiposColaboracionIds);

            colaborador.setNombre(datos.getNombre());
            colaborador.setApellido(datos.getApellido());
            colaborador.setFechaNacimiento(datos.getFechaNacimiento());
            colaborador.setTipoDocumento(datos.getTipoDocumento());
            colaborador.setDocumento(datos.getDocumento());

            colaborador.setContactos(datos.getContactos());
            if (datos.getDireccion() != null && datos.getDireccion().getCodigo_postal() != null)
                colaborador.setDireccion(datos.getDireccion());

            colaborador.setTiposColaboracion(tiposColaboracion);
            colaborador.getUsuario().setNuevo(0);

            repository.save(colaborador);

            usuario.setNuevoUsuario(0);
            sesion.actualizarUsuarioAutenticado(usuario);

            return "redirect:/home?success=true";
        } else return "redirect/:login";
    }

    @PostMapping("/configuracion/juridico")
    @Transactional
    public String cargarConfiguracionPersonaJuridica(@ModelAttribute PerfilColaboradorDto datos,
                                                     @RequestParam List<Integer> tiposColaboracionIds,
                                                   Model model){
        try {
            UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
            if (usuario.getRol().equals("COLABORADOR_JURIDICO")) {
                ColaboradorJuridico colaborador = (ColaboradorJuridico) repository.findById(usuario.getUsuario().getId()).get();
                List<TipoColaboracion> tiposColaboracion = rTipoColaboracion.findAllById(tiposColaboracionIds);

                colaborador.setCuit(datos.getCuit());
                colaborador.setRubro(datos.getRubro());
                colaborador.setRazonSocial(datos.getRazonSocial());
                colaborador.setTipoJuridico(datos.getTipoJuridico());

                colaborador.setContactos(datos.getContactos());

                if (datos.getDireccion() != null && datos.getDireccion().getCodigo_postal() != null)
                    colaborador.setDireccion(datos.getDireccion());

                colaborador.setTiposColaboracion(tiposColaboracion);
                colaborador.getUsuario().setNuevo(0);

                repository.save(colaborador);

                usuario.setNuevoUsuario(0);
                sesion.actualizarUsuarioAutenticado(usuario);

                return "redirect:/home?success=true";
            } else return "redirect/:login";
        } catch (Exception e) {
            return "redirect:/home?error=true";
        }
    }

    @PostMapping("/suscripcion")
    @Transactional
    public String suscribirHeladera(@RequestParam("heladeras") List<Integer> heladeras,
                                    @RequestParam(name = "preferencias", required = false) List<PreferenciaNotificacion> preferencias,
                                    @RequestParam(name = "cantidad_POCAS_VIANDAS", required = false) Integer pocasViandas,
                                    @RequestParam(name = "cantidad_HELADERA_LLENA", required = false) Integer heladeraLlena,
                                    Model model){
        try {
            UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
            ColaboradorFisico colaborador = (ColaboradorFisico) repository.findById(usuario.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

            List<Heladera> heladerasAnteriores = heladeraRepository.findBySuscriptoresContains(colaborador);
            for (Heladera h : heladerasAnteriores) {
                h.getSuscriptores().remove(colaborador);
                heladeraRepository.save(h);
            }

            List<Heladera> heladerasEncontradas = heladeraRepository.findAllById(heladeras);

            for (Heladera heladera : heladerasEncontradas) {
                heladera.agregarSuscriptor(colaborador);
                heladeraRepository.save(heladera);
            }

            // Procesar las preferencias de notificación
            Map<PreferenciaNotificacion, Integer> nuevasPreferencias = new HashMap<>();
            if (preferencias != null) {
                for (PreferenciaNotificacion pref : preferencias) {
                    int valor = switch (pref) {
                        case POCAS_VIANDAS -> (pocasViandas != null) ? pocasViandas : 0;
                        case HELADERA_LLENA -> (heladeraLlena != null) ? heladeraLlena : 0;
                        default -> 0;
                    };
                    nuevasPreferencias.put(pref, valor);
                }
            }

            colaborador.setPreferenciasNotif(nuevasPreferencias);
            repository.save(colaborador);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            return "redirect:/home?error=true";
        }
    }

    /* OBSOLETOOOO
    // Formulario de alta para el colaborador, filtrando por tipo (acá ya registró su usuario y se le preguntó por su tipo de colaborador)
    @GetMapping("/alta/{tipoColaborador}")
    public String obtenerFormulario(@PathVariable int tipoColaborador, Model model){
        // 0: persona humana; 1: persona juridica
        String tipo = tipoColaborador == 0 ? "PERSONA_HUMANA" : "PERSONA_JURIDICA";
        Formulario formulario = serviceFormulario.obtenerFormularioPorTipo(tipo);
        model.addAttribute("formularioColaborador", formulario);
        model.addAttribute("tipoColaborador", tipoColaborador);
        model.addAttribute("tipoContacto", MedioNotificacion.values());

        if (tipoColaborador == 1)
            model.addAttribute("tipoJuridico", TipoJuridico.values()); // persona jurídica
        else
            model.addAttribute("tipoDocumento", TipoDocumento.values()); // persona humana

        // devuelve las colaboraciones seleccionadas por la ONG por tipo de colaborador
        ColaboradorColaboracion colaboraciones = serviceColaboracion.obtenerFormularioPorTipo(tipo);
        model.addAttribute("tiposColaboracion", colaboraciones.getTipoColaboracion());
        return "formularioColaborador"; // esto carga los datos en el formularioColaborador.html
    }

    @PostMapping("/submitAlta/0")
    public String submitAltaCHumano(@ModelAttribute ColaboradorFisico colaborador,
                                     @RequestParam("opcionesColaboracion") List<Integer> opcionesColaboracion,
                                      @ModelAttribute Contacto contacto,
                                      @ModelAttribute Direccion direccion,
                                      Model model) {
        List<TipoColaboracion> colaboracionesSeleccionadas = opcionesColaboracion.stream()
                .map(id -> serviceColaboracion.findById(Integer.valueOf(id), "PERSONA_HUMANA"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        colaborador.agregarContacto(contacto);
        colaborador.setTiposColaboracion(colaboracionesSeleccionadas);
        colaborador.setDireccion(direccion);
        return "redirect:/colaborador/confirmar"; // Redirigir a una página de confirmación o éxito
    }

    @PostMapping("/submitAlta/1")
    public String submitAltaCJuridico(@ModelAttribute ColaboradorJuridico colaborador,
                                      @RequestParam("opcionesColaboracion") List<Integer> opcionesColaboracion,
                                     @ModelAttribute Contacto contacto,
                                     @ModelAttribute Direccion direccion,
                                     Model model) {
        List<TipoColaboracion> colaboracionesSeleccionadas = opcionesColaboracion.stream()
                .map(id -> serviceColaboracion.findById(Integer.valueOf(id), "PERSONA_JURIDICA"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        colaborador.agregarContacto(contacto);
        colaborador.setTiposColaboracion(colaboracionesSeleccionadas);
        colaborador.setDireccion(direccion);
        return "redirect:/colaborador/confirmar"; // Redirigir a una página de confirmación o éxito
    }
     */

    @GetMapping("/confirmar")
    public String mostrarConfirmacion(Model model) {
        return "confirmarCarga";
    }

}
