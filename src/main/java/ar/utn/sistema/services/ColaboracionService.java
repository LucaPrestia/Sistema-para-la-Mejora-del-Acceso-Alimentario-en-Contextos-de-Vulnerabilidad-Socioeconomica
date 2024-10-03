package ar.utn.sistema.services;

import ar.utn.sistema.entities.ColaboradorColaboracion;
import ar.utn.sistema.entities.colaboracion.TipoColaboracion;
import ar.utn.sistema.model.ColaboradorColaboracionConfig;
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
public class ColaboracionService {
    // TODO: cuando se integre la base de datos esto dejará de cargarse desde un form y se cargará desde la tabla de la base de datos
    private Map<String, ColaboradorColaboracion> colaboraciones = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        cargarColaboracionesXColaborador();
    }

    private void cargarColaboracionesXColaborador() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new ClassPathResource("config/colaboraciones.json").getInputStream();
        ColaboradorColaboracionConfig config = mapper.readValue(is, ColaboradorColaboracionConfig.class);
        colaboraciones = config.getFormColaboracion().stream()
                .collect(Collectors.toMap(ColaboradorColaboracion::getTipoColaborador, f -> f));
    }

    public ColaboradorColaboracion obtenerFormularioPorTipo(String tipoColaborador) {
        return colaboraciones.get(tipoColaborador);
    }

    public boolean cambiarColaboradorColaboracion(String tipoColaborador, TipoColaboracion[] colaboraciones){
        ColaboradorColaboracion colaboradorColaboraciones = this.colaboraciones.get(tipoColaborador);
        if (colaboradorColaboraciones != null){
            colaboradorColaboraciones.getTipoColaboracion().clear();
            for (TipoColaboracion t: colaboraciones)
                colaboradorColaboraciones.getTipoColaboracion().add(t);
            persistirCambios();
            return true;
        } else return false;
    }

    private void persistirCambios() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ColaboradorColaboracionConfig c = new ColaboradorColaboracionConfig();
            c.setFormColaboracion(new ArrayList<>(this.colaboraciones.values()));
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/config/colaboraciones.json"), c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TipoColaboracion findById(Integer id, String tipoColaborador) {
        // TODO: esto irá a consultar al repo de la base de datos
        ColaboradorColaboracion c = obtenerFormularioPorTipo(tipoColaborador);
        return c.getTipoColaboracion().stream().filter(t -> t.getId() == id).findFirst().get();
    }
}
