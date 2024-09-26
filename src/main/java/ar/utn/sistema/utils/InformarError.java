package ar.utn.sistema.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InformarError {
    private Map<String, Map<String, String>> erroresPorSeccion = new HashMap<>();

    // Agregar error a una sección específica
    public void addError(String seccion, String codigo, String mensaje) {
        erroresPorSeccion
                .computeIfAbsent(seccion, k -> new HashMap<>())
                .put(codigo, mensaje);
    }

    // Obtener errores de una sección específica
    public Map<String, String> getErroresPorSeccion(String seccion) {
        return erroresPorSeccion.getOrDefault(seccion, new HashMap<>());
    }

    public boolean hasErrors(){
        return !erroresPorSeccion.isEmpty();
    }

    public boolean hasErrorsInSection(String seccion) {
        return erroresPorSeccion.containsKey(seccion) && !erroresPorSeccion.get(seccion).isEmpty();
    }

}
