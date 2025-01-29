package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.PerfilColaboradorDto;
import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.ColaboracionService;
import ar.utn.sistema.services.FormularioService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @PostMapping("/configuracion/humano")
    public String cargarConfiguracionPersonaHumana(@ModelAttribute PerfilColaboradorDto datos,
                                                   @RequestParam List<Integer> tiposColaboracionIds,
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
            colaborador.setDireccion(datos.getDireccion());
            colaborador.setTiposColaboracion(tiposColaboracion);
            colaborador.getUsuario().setNuevo(0);

            repository.save(colaborador);

            usuario.setNuevoUsuario(0);
            sesion.actualizarUsuarioAutenticado(usuario);

            return "redirect:/home";
        } else return "redirect/:login";
    }

    @PostMapping("/configuracion/juridico")
    public String cargarConfiguracionPersonaJuridica(@ModelAttribute PerfilColaboradorDto datos,
                                                     @RequestParam List<Integer> tiposColaboracionIds,
                                                   Model model){
        UsuarioSesionDetalle usuario = sesion.obtenerUsuarioAutenticado();
        if(usuario.getRol().equals("COLABORADOR_JURIDICO")){
            ColaboradorJuridico colaborador = (ColaboradorJuridico) repository.findById(usuario.getUsuario().getId()).get();
            List<TipoColaboracion> tiposColaboracion = rTipoColaboracion.findAllById(tiposColaboracionIds);

            colaborador.setCuit(datos.getCuit());
            colaborador.setRubro(datos.getRubro());
            colaborador.setRazonSocial(datos.getRazonSocial());
            colaborador.setTipoJuridico(datos.getTipoJuridico());

            colaborador.setContactos(datos.getContactos());
            colaborador.setDireccion(datos.getDireccion());
            colaborador.setTiposColaboracion(tiposColaboracion);
            colaborador.getUsuario().setNuevo(0);

            repository.save(colaborador);

            usuario.setNuevoUsuario(0);
            sesion.actualizarUsuarioAutenticado(usuario);

            return "redirect:/home";
        } else return "redirect/:login";
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
        colaborador.setDireccion(direccion); // TODO: corroborar que tenga datos y que estén bien cargados!!
        // TODO: persistir en la base de datos!!!
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
        colaborador.setDireccion(direccion); // TODO: corroborar que tenga datos y que estén bien cargados!!
        // TODO: persistir en la base de datos!!!
        return "redirect:/colaborador/confirmar"; // Redirigir a una página de confirmación o éxito
    }
     */

    @GetMapping("/confirmar")
    public String mostrarConfirmacion(Model model) {
        return "confirmarCarga";
    }

}
