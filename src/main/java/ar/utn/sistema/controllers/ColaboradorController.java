package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.TipoColaboracion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.services.FormularioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/colaborador")
public class ColaboradorController {

    private static Logger logger = LoggerFactory.getLogger(ColaboradorController.class);
    @Autowired
    private FormularioService service;

    // Formulario de alta para el colaborador, filtrando por tipo (acá ya registró su usuario y se le preguntó por su tipo de colaborador)
    @GetMapping("/alta/{tipoColaborador}")
    public String obtenerFormulario(@PathVariable int tipoColaborador, Model model){
        // 0: persona humana; 1: persona juridica
        String tipo = tipoColaborador == 0 ? "PERSONA_HUMANA" : "PERSONA_JURIDICA";
        Formulario formulario = service.obtenerFormularioPorTipo(tipo);
        model.addAttribute("formularioColaborador", formulario);
        model.addAttribute("tipoColaborador", tipoColaborador);
        model.addAttribute("tipoContacto", MedioNotificacion.values());

        if (tipoColaborador == 1)
            model.addAttribute("tipoJuridico", TipoJuridico.values()); // persona jurídica
        else
            model.addAttribute("tipoDocumento", TipoDocumento.values()); // persona humana

        // TODO: consultar en la base de datos los tipos de colaboración que le corresponde a cada tipo de colaborador; por ahora mando todos los existentes:
        model.addAttribute("tiposColaboracion", TipoColaboracion.values());
        return "formularioColaborador"; // esto carga los datos en el formularioColaborador.hbs
    }

    @PostMapping("/submitAlta/0")
    public String submitAltaCHumano(@ModelAttribute ColaboradorFisico colaborador,
                                     @RequestParam("opcionesColaboracion") TipoColaboracion[] opcionesColaboracion,
                                      @ModelAttribute Contacto contacto,
                                      @ModelAttribute Direccion direccion,
                                      Model model) {
        logger.info(String.valueOf(colaborador));
        logger.info(Arrays.toString(opcionesColaboracion));
        logger.info(String.valueOf(contacto));
        colaborador.agregarContacto(contacto);
        // TODO: ver cómo persistir los tipos de colaboraciones que seleccionó el usuario
        colaborador.setDireccion(direccion); // TODO: corroborar que tenga datos y que estén bien cargados!!
        // TODO: persistir en la base de datos!!!
        return "redirect:/colaborador/confirmacion"; // Redirigir a una página de confirmación o éxito
    }

    @PostMapping("/submitAlta/1")
    public String submitAltaCJuridico(@ModelAttribute ColaboradorJuridico colaborador,
                                     @RequestParam("opcionesColaboracion") TipoColaboracion[] opcionesColaboracion,
                                     @ModelAttribute Contacto contacto,
                                     @ModelAttribute Direccion direccion,
                                     Model model) {
        logger.info(String.valueOf(colaborador));
        logger.info(Arrays.toString(opcionesColaboracion));
        logger.info(String.valueOf(contacto));
        colaborador.agregarContacto(contacto);
        // TODO: ver cómo persistir los tipos de colaboraciones que seleccionó el usuario
        colaborador.setDireccion(direccion); // TODO: corroborar que tenga datos y que estén bien cargados!!
        // TODO: persistir en la base de datos!!!
        return "redirect:/colaborador/confirmacion"; // Redirigir a una página de confirmación o éxito
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        return "confirmacionColaborador"; // TODO: AGREGAR HBS
    }

}
