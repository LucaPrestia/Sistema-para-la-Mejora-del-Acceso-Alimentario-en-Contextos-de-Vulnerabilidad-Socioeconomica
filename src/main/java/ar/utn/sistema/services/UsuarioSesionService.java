package ar.utn.sistema.services;

import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.entities.usuarios.requisitosContrasena.Requisitos;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.AdminRepository;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.TecnicoRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioSesionService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ColaboradorRepository rColaborador;
    @Autowired
    private TecnicoRepository rTecnico;
    @Autowired
    private AdminRepository rAdmin;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public UsuarioSesionService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Rol rol;
        switch (usuario.getRol()){
            case "COLABORADOR_FISICO": case "COLABORADOR_JURIDICO":
                rol = rColaborador.findByUsuario_Id(usuario.getId()).orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
                break;
            case "TECNICO":
                rol = rTecnico.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
                break;
            case "ADMIN":
                rol = rAdmin.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Admin no encontrado"));
                break;
            default:
                throw new IllegalArgumentException("Rol no válido");
        }

        return new UsuarioSesionDetalle(
                usuario.getId(),
                usuario.getRol(),
                usuario.getNuevo(),
                rol,
                usuario.getUsuario(),
                usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())));
    }

    public Optional<String> controlarRequisitosContrasenia(String pass) {
        Usuario nuevoUsuario = new Usuario();
        return nuevoUsuario.getRequisitos().stream()
                .filter(req -> !req.evaluarContrasena(pass))
                .map(Requisitos::getMensajeError)
                .findFirst();
    }
    public UsuarioSesionDetalle obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (UsuarioSesionDetalle) authentication.getPrincipal();
        }
        return null;
    }

    public void actualizarUsuarioAutenticado(UsuarioSesionDetalle nuevoUsuarioSesionDetalle) {
        // Obtener el Authentication actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Obtener las credenciales y autoridades del Authentication actual
            Object credentials = authentication.getCredentials();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Crear un nuevo Authentication con el nuevo UsuarioSesionDetalle
            Authentication nuevaAuthentication = new UsernamePasswordAuthenticationToken(
                    nuevoUsuarioSesionDetalle, // El nuevo UsuarioSesionDetalle
                    credentials,                // Las mismas credenciales
                    authorities                 // Las mismas autoridades
            );

            // Actualizar el contexto de seguridad con el nuevo Authentication
            SecurityContextHolder.getContext().setAuthentication(nuevaAuthentication);
        }
    }

    public Usuario registrarUsuario(String user, String pass, String rol) {
        if (usuarioRepository.findByUsuario(user).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existente. Seleccione otro.");
        }

        Optional<String> error = this.controlarRequisitosContrasenia(pass);
        if (error.isPresent()) {
            throw new IllegalArgumentException(error.get());
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsuario(user);
        nuevoUsuario.setContrasena(passwordEncoder.encode(pass));
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setNuevo(1);
        switch (rol) {
            case "COLABORADOR_FISICO":
                Colaborador rolClase = new ColaboradorFisico(nuevoUsuario);
                rColaborador.save(rolClase);
                break;
            case "COLABORADOR_JURIDICO":
                Colaborador rolClasea = new ColaboradorJuridico(nuevoUsuario);
                rColaborador.save(rolClasea);
                break;
            case "TECNICO":
                /*    Tecnico rolClase = new Tecnico(nuevoUsuario);
                    rColaborador.save(rolClase);*/
                break;
            case "ADMIN":
                // rolClase = new Ong();
                break;
            default:
                throw new IllegalArgumentException("Rol no válido");
        }
        return nuevoUsuario;
    }

    public void cambiarContrasenia(String username, String passwordVieja, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);

        if (usuario.isPresent()) {
            Usuario usuarioLogueado = usuario.get();

            if (passwordEncoder.matches(passwordVieja, usuarioLogueado.getContrasena())) {
                Optional<String> error = this.controlarRequisitosContrasenia(password);
                if (error.isPresent()) {
                    throw new IllegalArgumentException(error.get());
                }

            usuarioLogueado.setContrasena(passwordEncoder.encode(password));
            usuarioLogueado.setNuevo(0);
            usuarioRepository.save(usuarioLogueado);
        } else {
            throw new IllegalArgumentException("Contraseña actual inválida. Por favor, vuelva a ingresar los datos");
        }
        } else {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }
}
