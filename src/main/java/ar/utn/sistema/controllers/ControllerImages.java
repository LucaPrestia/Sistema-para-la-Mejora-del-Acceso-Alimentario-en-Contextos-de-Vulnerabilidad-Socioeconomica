package ar.utn.sistema.controllers;

import ar.utn.sistema.entities.colaboracion.OfertaCanje;
import ar.utn.sistema.entities.incidente.IncidenteFallaTecnica;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.OfertaCanjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/imagenes")
public class ControllerImages {
    @Autowired
    private  OfertaCanjeRepository ofertaCanjeRepository;

    @Autowired
    private IncidenteRepository incidenteRepository;


    @GetMapping("/oferta/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Integer id) {
        Optional<OfertaCanje> oferta = ofertaCanjeRepository.findById(id);

        if (oferta.isPresent() && oferta.get().getImagen() != null) {
            byte[] imagenBytes = oferta.get().getImagen();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png"); // O ajusta el tipo de imagen según corresponda
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/incidente/{id}")
    public ResponseEntity<byte[]> obtenerImagenIncidente(@PathVariable Integer id) {
        Optional<IncidenteFallaTecnica> incidente = incidenteRepository.findFallaTecnicaById(id);

        if (incidente.isPresent() && incidente.get().getFoto() != null) {
            byte[] imagenBytes = incidente.get().getFoto();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}