package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DTOVulnerablesXLocalidad;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import ar.utn.sistema.repositories.PersonaVulnerableRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ServicioObtenerVulnerablesXLocalidad {
    @Autowired
    public PersonaVulnerableRespository personaVulnerableRespository;
    @GetMapping("/VulnerablesXLocalidad")
    @ResponseBody
    public List<DTOVulnerablesXLocalidad> dtoVulnerablesXLocalidads() {
        List<PersonaVulnerable> todasLasPersonas = personaVulnerableRespository.findAll();
        List<DTOVulnerablesXLocalidad> respuesta = new ArrayList<>();

        for (PersonaVulnerable persona : todasLasPersonas) {
            // Obtener la localidad de la persona
            String localidad = persona.getDireccion().getLocalidad();

            // Verificar si ya existe un DTO para esta localidad
            Optional<DTOVulnerablesXLocalidad> dtoExistente = respuesta.stream()
                    .filter(dto -> Objects.equals(dto.getLocalidad(), localidad))
                    .findFirst();

            if (dtoExistente.isPresent()) {
                // Si el DTO para esta localidad ya existe, incrementar el contador
                dtoExistente.get().setPersonaVulnerables(dtoExistente.get().getPersonaVulnerables() + 1);
            } else {
                // Si no existe, crear un nuevo DTO para esta localidad con contador en 1
                DTOVulnerablesXLocalidad nuevoDto = new DTOVulnerablesXLocalidad();
                nuevoDto.setLocalidad(localidad);
                nuevoDto.setPersonaVulnerables(1);
                respuesta.add(nuevoDto);
            }
        }

        return respuesta;
    }


}
