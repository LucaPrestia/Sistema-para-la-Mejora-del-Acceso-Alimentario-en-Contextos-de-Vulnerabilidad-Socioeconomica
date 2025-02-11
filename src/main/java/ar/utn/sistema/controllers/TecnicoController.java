package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.TecnicoAltaDto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.Tecnico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.TecnicoRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private TecnicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public TecnicoController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/alta")
    public String altaTecnico(@ModelAttribute TecnicoAltaDto datos, RedirectAttributes redirectAttributes){
        try {
            // todo: realizar validaciones de carga (que esté cargada la dire, al menos un contacto, etc...)
            if(usuarioRepository.findByUsuario(datos.getUsername()).isPresent()){
                redirectAttributes.addFlashAttribute("errorMessage", "Nombre de usuario existente, elegir otro para el técnico a registrar");
                /*redirectAttributes.addFlashAttribute("sectorCentral", "fragments/administrador :: registrarTecnico");
                redirectAttributes.addFlashAttribute("tipoDocumento", TipoDocumento.values());
                redirectAttributes.addFlashAttribute("tipoContacto", MedioNotificacion.values());*/ // no funciono
                return "redirect:/home?error=true";
            }
            Usuario usuario = new Usuario(
                    datos.getUsername(),
                    passwordEncoder.encode(datos.getDocumento().toString()),             // por ahora como contraseña encripto el documento!
                    "TECNICO");
            Tecnico tecnico = new Tecnico(
                    usuario,
                    datos.getNombre(),
                    datos.getApellido(),
                    datos.getTipoDocumento(),
                    datos.getDocumento(),
                    datos.getCuil(),
                    datos.getContactos().get(0),
                    datos.getDireccion().getLocalidad(),
                    datos.getDireccion()
                    );
            repository.save(tecnico);
            // todo: mandar notificacion al tecnico a traves del boot con credenciales de acceso
            return "redirect:/home?success=true";
        } catch (Exception e) {
            return "redirect:/home?error=true";
        }
    }

}
