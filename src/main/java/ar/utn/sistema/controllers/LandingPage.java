package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.services.ColaboracionService;
import ar.utn.sistema.services.FormularioService;
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
public class LandingPage {

    private static Logger logger = LoggerFactory.getLogger(LandingPage.class);


    @GetMapping("/")
    public String irALanding(Model model) {
        return "landing";
    }

}
