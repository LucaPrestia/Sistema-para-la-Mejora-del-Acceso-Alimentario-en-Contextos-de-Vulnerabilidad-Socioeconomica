package ar.utn.sistema.services;

import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CoeficientesColaboracionService {
    private final Map<String, Double> coeficientes = new HashMap<>();

    @Autowired
    private CoeficientesColaboracionRepository repository;

    @PostConstruct
    private void cargarCoeficientes() {
        // Carga inicial desde la base de datos
        List<CoeficientesColaboracion> coeficientesList = repository.findAll();
        coeficientesList.forEach(c -> coeficientes.put(c.getTipoColaboracion().getCodigo(), c.getCoeficientePuntos()));
    }

    public void actualizarCoeficiente(Integer id, Double coeficiente){
        Optional<CoeficientesColaboracion> coefColaboracion = repository.findById(id);

        if (coefColaboracion.isPresent()) {
            CoeficientesColaboracion coeficienteActualizado = coefColaboracion.get();
            coeficienteActualizado.setCoeficientePuntos(coeficiente);
            repository.save(coeficienteActualizado);

            String codigo = coeficienteActualizado.getTipoColaboracion().getCodigo();
            coeficientes.put(codigo, coeficiente);
        } else {
            throw new IllegalArgumentException("El coeficiente con el ID " + id + " no existe.");
        }
    }

    public Double obtenerCoeficiente(String codigo) {
        return coeficientes.get(codigo);
    }

}
