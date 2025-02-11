package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.CoeficienteDTO;
import ar.utn.sistema.dto.ConfigSistemaDTO;
import ar.utn.sistema.dto.ParametroDTO;
import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.configuracion.ParametrosGenerales;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.ColaboradorJuridico;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ColaboradorColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ParametrosGeneralesRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private CoeficientesColaboracionRepository coeficientesRepository;
    @Autowired
    private ParametrosGeneralesRepository parametrosRepository;
    @Autowired
    private ColaboradorColaboracionRepository colaboradorColaboracionRepository;
    @Autowired
    private TipoColaboracionRepository tipoColaboracionRepository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Transactional // o se actualiza (t o d o) o nada, convierte la operación a atómica
    public void actualizarConfiguracion(ConfigSistemaDTO datos) {
        // coeficientes
        for (CoeficienteDTO coef : datos.getCoeficientes()) {
            coeficientesRepository.findById(coef.getId()).ifPresent(existente -> {
                existente.setCoeficientePuntos(coef.getCoeficientePuntos());
                coeficientesRepository.save(existente);
            });
        }
        // parametros generales
        for (ParametroDTO parametro : datos.getParametros()) {
            parametrosRepository.findById(parametro.getId()).ifPresent(existente -> {
                existente.setValor(parametro.getValor());
                parametrosRepository.save(existente);
            });
        }

        // colaboraciones
        actualizarColaboraciones(datos.getColaboracionesHumano(), "PERSONA_HUMANA", ColaboradorFisico.class);
        actualizarColaboraciones(datos.getColaboracionesJuridico(), "PERSONA_JURIDICA", ColaboradorJuridico.class);
    }

    private void actualizarColaboraciones(List<String> nuevasColaboraciones, String tipo, Class<? extends Colaborador> tipoColaborador) {
        ColaboradorColaboracion colaborador = colaboradorColaboracionRepository.findByTipoColaborador(tipo)
                .orElse(new ColaboradorColaboracion());

        colaborador.setTipoColaborador(tipo);

        List<TipoColaboracion> colaboracionesActuales = tipoColaboracionRepository.findByTipoColaborador(tipo);

        Set<String> nuevasColaboracionesSet = nuevasColaboraciones != null ? new HashSet<>(nuevasColaboraciones) : new HashSet<>();
        Set<String> actualesSet = colaboracionesActuales.stream()
                .map(TipoColaboracion::getCodigo)
                .collect(Collectors.toSet());

        List<TipoColaboracion> colaboracionesAEliminar = colaboracionesActuales.stream()
                .filter(tc -> !nuevasColaboracionesSet.contains(tc.getCodigo()))
                .toList();

        List<TipoColaboracion> colaboracionesAAgregar = tipoColaboracionRepository.findByCodigoIn(nuevasColaboraciones).stream()
                .filter(tc -> !actualesSet.contains(tc.getCodigo()))
                .toList();

        colaborador.getTipoColaboracion().removeAll(colaboracionesAEliminar);
        colaborador.getTipoColaboracion().addAll(colaboracionesAAgregar);

        if (!colaboracionesAEliminar.isEmpty()) {
            List<Colaborador> colaboradores = colaboradorRepository.findByTipoColaborador(tipoColaborador);
            for (Colaborador c : colaboradores) {
                c.getTiposColaboracion().removeAll(colaboracionesAEliminar);
            }
            colaboradorRepository.saveAll(colaboradores);
        }

        colaboradorColaboracionRepository.save(colaborador);
    }

    @PostMapping("/configSistema")
    public String parametrizarSistema(@ModelAttribute ConfigSistemaDTO datos,
                                      Model model){
        try {
            actualizarConfiguracion(datos);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            // Log del error para depuración
            logger.error("Error al parametrizar el sistema: ", e);
            return "redirect:/home?error=true";
        }
    }

}
