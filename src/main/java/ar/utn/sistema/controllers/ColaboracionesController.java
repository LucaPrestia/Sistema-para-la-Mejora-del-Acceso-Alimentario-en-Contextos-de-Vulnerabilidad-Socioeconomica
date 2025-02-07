package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.*;
import ar.utn.sistema.entities.Coordenadas;
import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.colaboracion.*;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.ServicioDeUbicacionHeladera;
import ar.utn.sistema.entities.heladera.Vianda;
import ar.utn.sistema.entities.tarjeta.TarjetaPersonaVulnerable;
import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.repositories.*;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import ar.utn.sistema.services.CoeficientesColaboracionService;
import ar.utn.sistema.services.HeladeraService;
import ar.utn.sistema.services.UsuarioSesionService;
import org.hibernate.event.internal.EvictVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller()
@RequestMapping("/colaboracion")
public class ColaboracionesController {


    @Autowired
    private ColaboracionRepository colaboracionRepository;

    @Autowired
    private UsuarioSesionService sesion;
    @Autowired
    private CoordenadasRepository coordenadasRepository;
    @Autowired
    private HeladeraRepository heladeraRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CoeficientesColaboracionRepository coeficientesColaboracionRepository;
    @Autowired
    private TipoColaboracionRepository tipoColaboracionRepository;
    @Autowired
    private ViandaRepository viandaRepository;
    @Autowired
    private CoeficientesColaboracionService coeficientesColaboracionService;
    @Autowired
    private PersonaVulnerableRespository personaVulnerableRespository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private HeladeraService heladeraService;

    private Colaborador obtenerColaborador(){
        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        return colaborador;
    }
    @PostMapping("/colocarHeladera")
    public String guardarHeladeraColocar(
            @RequestParam("nombre") String nombre,
            @RequestParam("tempMin") double tempMin,
            @RequestParam("tempMax") double tempMax,
            @RequestParam("maxViandas") int maxViandas,
            @RequestParam("direccionSeleccionada") Integer direccionId,
            Model model) {
        try {
            Direccion direccion = direccionRepository.findById(direccionId).get();
            System.out.println(direccion.getCalle());
            System.out.println(direccion.getId());
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            System.out.println(owner.getId());
            Heladera nuevaHeladera = new Heladera(nombre, owner, direccion, tempMax, tempMin, maxViandas);
            heladeraRepository.save(nuevaHeladera);
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.GESTION_HELADERA.name());
            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(nuevaHeladera,coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            Colaborador colaborador = obtenerColaborador();
            colaborador.agregarColaboracion(colaboracionGestionHeladera);
            colaboradorRepository.save(colaborador);

            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }


    @PostMapping("/hacerseCargoHeladera")
    public String guardarHacerseCargoHeladera(@RequestParam("direccionSeleccionada") Integer heladeraId, Model model) {
        try {
            Heladera heladera = heladeraRepository.findById(heladeraId).get();
            Usuario owner  = usuarioRepository.findById(sesion.obtenerUsuarioAutenticado().getId()).get();
            System.out.println(owner.getId());
            heladera.setOwner(owner);
            heladeraRepository.save(heladera);
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.GESTION_HELADERA.name());
            ColaboracionGestionHeladera colaboracionGestionHeladera = new ColaboracionGestionHeladera(heladera, coeficientePuntos);
            colaboracionRepository.save(colaboracionGestionHeladera);
            obtenerColaborador().agregarColaboracion(colaboracionGestionHeladera);
            Colaborador colaborador = obtenerColaborador();
            colaborador.agregarColaboracion(colaboracionGestionHeladera);
            colaboradorRepository.save(colaborador);

            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }

    @PostMapping("/donacionDinero")
    public String guardarHacerDonacionDinero(
            @RequestParam("dinero") double dinero,
            @RequestParam("frecuenciaSeleccionada") String id_frecuencia, Model model) {
        try {

            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.DINERO.name());
            System.out.println(coeficientePuntos);
            ColaboracionDinero colaboracionDinero = new ColaboracionDinero((float) dinero, TipoFrecuencia.valueOf(id_frecuencia), coeficientePuntos);
            System.out.println(colaboracionDinero);
            colaboracionRepository.save(colaboracionDinero);
            Colaborador colaborador = obtenerColaborador();
            colaborador.agregarColaboracion(colaboracionDinero);
            colaboradorRepository.save(colaborador);

            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);

            return "redirect:/home?error=true";
        }
    }
    @PostMapping("/donacionVianda")
    public String recibirDonacion(@RequestBody DonacionViandaDTO donacion, Model model) {
        // todo: verificar que hay espacio suficiente para guardar las viandas
        System.out.println("Heladera Seleccionada: " + donacion.getHeladeraSeleccionada());
        for (ViandaDTO vianda : donacion.getViandas()) {
            System.out.println("Comida: " + vianda.getComida());
            System.out.println("Fecha Caducidad: " + vianda.getFechaCaducidad());
            System.out.println("Calorías: " + vianda.getCalorias());
            System.out.println("Peso: " + vianda.getPeso());
        }
        try {
            // Lógica de procesamiento como la que ya tienes
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.DONACION_VIANDAS.name());
            System.out.println(coeficientePuntos);

            List<Vianda> viandasNuevas = new ArrayList<>();
            Heladera heladera = heladeraRepository.findById(donacion.getHeladeraSeleccionada()).get();
            for (ViandaDTO vianda : donacion.getViandas()) {
                Vianda nuevaVianda = new Vianda(vianda.getComida(), vianda.getFechaCaducidad(), heladera, vianda.getCalorias(), vianda.getPeso());
                viandasNuevas.add(nuevaVianda);
            }
            heladeraService.agregarViandas(heladera, viandasNuevas);
            // viandaRepository.saveAll(viandasNuevas); // ya se graba desde la línea anterior, en agregarViandas, por relación cascada
            ColaboracionVianda colaboracionVianda = new ColaboracionVianda(viandasNuevas, viandasNuevas.size(), coeficientePuntos);
            System.out.println(colaboracionVianda);
            colaboracionRepository.save(colaboracionVianda);
            Colaborador colaborador = obtenerColaborador();
            colaborador.agregarColaboracion(colaboracionVianda);
            colaboradorRepository.save(colaborador);

            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }
    @PostMapping("/donacionPersonaVulnerable")
    public String recibirDonacion(@RequestBody DonacionPersonaVulnerableDTO donacion,Model model) {
        for (PersonaVulnerableDTO personaVulnerableDTO : donacion.getPersonaVulnerable()) {
            System.out.println("Nombre: " + personaVulnerableDTO.getNombre());
            System.out.println("Fecha Nacimiento: " + personaVulnerableDTO.getFechaNacimiento());
            System.out.println("Fecha Registro: " + personaVulnerableDTO.getFechaRegistro());
            System.out.println("Situación de Calle: " + personaVulnerableDTO.getSituacionDeCalle());
            System.out.println("Dirección: " + personaVulnerableDTO.getCalle() + " " + personaVulnerableDTO.getNumero() + ", " + personaVulnerableDTO.getLocalidad() + ", " + personaVulnerableDTO.getProvincia() + ", " + personaVulnerableDTO.getPais() + " - Código Postal: " + personaVulnerableDTO.getCodigoPostal());
        }

        try {
            // Aquí va la lógica de procesamiento como guardado en la base de datos
            List<PersonaVulnerable> personaVulnerableList = new ArrayList<>();
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.ENTREGA_TARJETAS.name());
            ColaboracionTarjeta colaboracionTarjeta = new ColaboracionTarjeta(donacion.getPersonaVulnerable().size(),coeficientePuntos);
            ColaboradorFisico colaborador = (ColaboradorFisico) colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).get();
            for (PersonaVulnerableDTO dto : donacion.getPersonaVulnerable()) {
                PersonaVulnerable personaVulnerable = new PersonaVulnerable();
                personaVulnerable.setNombre(dto.getNombre());
                personaVulnerable.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
                personaVulnerable.setFechaRegistro(LocalDate.parse(dto.getFechaRegistro()));
                personaVulnerable.setSituacionDeCalle(dto.getSituacionDeCalle());
                Direccion direccion = new Direccion();
                direccion.setPais(dto.getPais());
                direccion.setProvincia(dto.getProvincia());
                direccion.setLocalidad(dto.getLocalidad());
                direccion.setCalle(dto.getCalle());
                direccion.setNumero(dto.getNumero());
                direccion.setDepartamento(dto.getDepartamento());
                direccion.setCodigo_postal(dto.getCodigoPostal());
                personaVulnerable.setDireccion(direccion);
                direccionRepository.save(direccion);
                personaVulnerableList.add(personaVulnerable);
                colaboracionTarjeta.registrarPersonaVulnerable(personaVulnerable, colaborador);
            }
            personaVulnerableRespository.saveAll(personaVulnerableList);
            colaboracionRepository.save(colaboracionTarjeta);
            colaborador.agregarColaboracion(colaboracionTarjeta);
            colaboradorRepository.save(colaborador);
            model.addAttribute("success", true);
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }


    @PostMapping("/ofrecerServicio")
    public String procesarFormulario(
            @RequestParam("nombreServicio") String nombre,
            @RequestParam("rubro") RubroServicio rubro,
            @RequestParam("puntosRequeridos") double puntosRequeridos,
            @RequestParam("imagen") MultipartFile imagen,
            Model model)
            throws IOException {
    try{

        Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                () -> new RuntimeException("Colaborador no encontrado")
        );
        Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.OFERTA_SERVICIO.name());

        System.out.println("Colaborador encontrado: " + colaborador);

        // Convertir la imagen a byte[]
        byte[] imagenBytes = imagen.getBytes();

        // Crear la nueva colaboración
        ColaboracionOfertaServicio nuevaColaboracion = new ColaboracionOfertaServicio(
                nombre, rubro, Math.round(puntosRequeridos * 100.0) / 100.0, imagenBytes, colaborador, coeficientePuntos
        );

        colaboracionRepository.save(nuevaColaboracion);
        colaborador.agregarColaboracion(nuevaColaboracion);
        colaboradorRepository.save(colaborador);
        return "redirect:/home?success=true";
    } catch (Exception e) {
        model.addAttribute("error", true);
        return "redirect:/home?error=true";
    }
    }
    @PostMapping("/distribuirVianda")
    public String distribuirViandas(
            @RequestParam("origenHeladeraId") Integer origenHeladeraId,
            @RequestParam("destinoHeladeraId") Integer destinoHeladeraId,
            @RequestParam("viandasIds") List<Integer> viandasIds,
            @RequestParam("motivoDistribucion") String motivoDistribucion, Model model
    ) {
        try {
            Heladera heladeraOrigen = heladeraRepository.findById(origenHeladeraId).orElseThrow(() -> new RuntimeException("Origen de heladera no encontrado"));
            Heladera heladeraDestino = heladeraRepository.findById(destinoHeladeraId).orElseThrow(() -> new RuntimeException("Origen de heladera no encontrado"));
            List<Vianda> viandasList = new ArrayList<>();
            for (Integer id : viandasIds) {
                Vianda vianda = viandaRepository.findById(id).orElseThrow(() -> new RuntimeException("Vianda no encontrado"));
                viandasList.add(vianda);
            }
            Double coeficientePuntos = coeficientesColaboracionService.obtenerCoeficiente(TipoColaboracionEnum.REDISTRIBUCION_VIANDAS.name());

            Colaborador colaborador = colaboradorRepository.findByUsuario_Id(sesion.obtenerUsuarioAutenticado().getId()).orElseThrow(
                    () -> new RuntimeException("Colaborador no encontrado")
            );
            ColaboracionDistribucionViandas nuevaColaboracion = new ColaboracionDistribucionViandas(heladeraOrigen, heladeraDestino, viandasList, motivoDistribucion, coeficientePuntos);
            heladeraService.sacarViandas(heladeraOrigen, viandasList);
            heladeraService.agregarViandas(heladeraDestino, viandasList);
            colaboracionRepository.save(nuevaColaboracion);
            colaborador.agregarColaboracion(nuevaColaboracion);
            colaboradorRepository.save(colaborador);
            /*viandaRepository.saveAll(viandasList);
            heladeraRepository.save(heladeraOrigen);
            heladeraRepository.save(heladeraDestino);*/ // ya se guardan con las funciones de sacarviandas y agregarviandas
            return "redirect:/home?success=true";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/home?error=true";
        }
    }

}
