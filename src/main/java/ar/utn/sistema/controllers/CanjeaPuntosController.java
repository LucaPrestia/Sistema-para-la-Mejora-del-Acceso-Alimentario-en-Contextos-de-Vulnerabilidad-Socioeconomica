package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DonacionViandaDTO;
import ar.utn.sistema.dto.ViandaDTO;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.*;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.CoeficientesColaboracionService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping()
public class CanjeaPuntosController {



    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private OfertaCanjeRepository ofertaCanjeRepository ;
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    private Colaborador obtenerColaborador(){
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        return colaborador;
    }
    @PostMapping("/canjear")
    public String canjearPuntos(@RequestParam int idOferta, Model model) {
        // Obtener colaborador autenticado
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElse(null);


        Optional<OfertaCanje> ofertaOpt = ofertaCanjeRepository.findById(idOferta);

        if (ofertaOpt.isPresent()) {
            OfertaCanje oferta = ofertaOpt.get();

            if (colaborador != null)
                if (colaborador.getPuntosDisponibles() >= oferta.getPuntosRequeridos()) {
                    colaborador.setPuntosDisponibles(colaborador.getPuntosDisponibles() - oferta.getPuntosRequeridos());
                    colaboradorRepository.save(colaborador);
                    model.addAttribute("success", "Canje realizado con éxito");
                } else {
                    model.addAttribute("error", "No tienes suficientes puntos para canjear esta oferta.");
                }
        } else {
            model.addAttribute("error", "Oferta no encontrada.");
        }
        return "redirect:/home";
    }


}
