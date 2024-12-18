package ar.utn.sistema.services;


import ar.utn.sistema.model.CampoFormulario;
import ar.utn.sistema.model.Formulario;
import ar.utn.sistema.model.FormulariosConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FormularioService {
    private Map<String, Formulario> formularios = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        cargarFormularios();
    }

    private void cargarFormularios() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new ClassPathResource("config/formularios.json").getInputStream();
        FormulariosConfig config = mapper.readValue(is, FormulariosConfig.class);
        formularios = config.getFormulariosColaborador().stream()
                .collect(Collectors.toMap(Formulario::getTipoColaborador, f -> f));
    }

    public Formulario obtenerFormularioPorTipo(String tipoColaborador) {
        return formularios.get(tipoColaborador);
    }

    public void agregarCampo(String tipoColaborador, CampoFormulario nuevoCampo) {
        Formulario formulario = formularios.get(tipoColaborador);
        if (formulario != null) {
            formulario.agregarCampo(nuevoCampo);
            guardarFormularios();
        }
    }

    public boolean eliminarCampo(String tipoColaborador, String nombreCampo) {
        Formulario formulario = formularios.get(tipoColaborador);
        // controla que el campo que se quiere eliminar no sea obligatorio
        if (formulario != null && formulario.getCampos().stream().filter(c -> c.getNombreCampo().equals(nombreCampo) && !c.getObligatorio()).findFirst().isPresent()) {
            formulario.setCampos(formulario.getCampos().stream()
                    .filter(c -> !c.getNombreCampo().equals(nombreCampo))
                    .collect(Collectors.toList()));
            guardarFormularios();
            return true;
        } else return false;
    }

    public boolean verificarExistenciaCampo(String tipoColaborador, String nombreCampo){
        Formulario formulario = formularios.get(tipoColaborador);
        return formulario.getCampos().stream().filter(c -> c.getNombreCampo().equals(nombreCampo)).findFirst().isPresent();
    }


    public void modificarCampo(String tipoColaborador, String nombreCampo, String nuevaEtiqueta, String nuevoTipo, boolean nuevoRequerido) {
        Formulario formulario = formularios.get(tipoColaborador);
        if (formulario != null) {
            for (CampoFormulario campo : formulario.getCampos()) {
                if (campo.getNombreCampo().equals(nombreCampo)) {
                    campo.setEtiqueta(nuevaEtiqueta);
                    campo.setTipo(nuevoTipo);
                    campo.setRequerido(nuevoRequerido);
                    break;
                }
            }
            guardarFormularios();
        }
    }
    private void guardarFormularios() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            FormulariosConfig config = new FormulariosConfig();
            config.setFormulariosColaborador(new ArrayList<>(formularios.values()));
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/config/formularios.json"), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
