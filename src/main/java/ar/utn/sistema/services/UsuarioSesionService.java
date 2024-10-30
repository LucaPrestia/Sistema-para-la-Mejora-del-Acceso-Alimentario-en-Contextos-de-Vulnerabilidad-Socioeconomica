package ar.utn.sistema.services;

import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.repositories.AdminRepository;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.TecnicoRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioSesionService implements UserDetailsService {

    // private final UsuarioRepository repository;
    // provisorio por no tener repositorios de la base de datos:
    private List<Usuario> usuarios;
    private final PasswordEncoder passwordEncoder;

    /*
    @Autowired
    private ColaboradorRepository colaboradorRepo;
    @Autowired
    private TecnicoRepository tecnicoRepo;
    @Autowired
    private AdminRepository adminRepo;
    */

    public UsuarioSesionService(/*UsuarioRepository repository,*/ PasswordEncoder passwordEncoder) {
        //this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.usuarios = List.of(
            new Usuario("colaborador", passwordEncoder.encode("colaborador123"), "COLABORADOR"),
            new Usuario("tecnico", passwordEncoder.encode("tecnico123"), "TECNICO"),
            new Usuario("admin", passwordEncoder.encode("admin123"), "ADMIN")
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getUsuario().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        //repository.findByUsuario(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Rol rol;
        switch (usuario.getRol()){
            case "COLABORADOR":
                rol = new ColaboradorFisico(); //colaboradorRepo.findByUsuario(usuario.getId()).orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
                break;
            case "TECNICO":
                rol = new Tecnico(); //tecnicoRepo.findByUsuario(usuario.getId()).orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
                break;
            case "ADMIN":
                rol = new Ong(); // adminRepo.findByUsuario(usuario.getId()).orElseThrow(() -> new RuntimeException("Admin no encontrado"));
                break;
            default:
                throw new IllegalArgumentException("Rol no válido");
        }

        return new UsuarioSesionDetalle(
                usuario.getRol(),
                rol,
                usuario.getUsuario(),
                usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())));
    }

    public boolean controlarRequisitosContrasenia(String pass){
        Usuario nuevoUsuario = new Usuario();
        return nuevoUsuario.getRequisitos().stream().allMatch(x-> x.evaluarContrasena(pass));
    }

    public UsuarioSesionDetalle obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (UsuarioSesionDetalle) authentication.getPrincipal();
        }
        return null;
    }

    public Usuario registrarUsuario(String user, String pass, String rol) {
        if(this.controlarRequisitosContrasenia(pass)){
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsuario(user);
            nuevoUsuario.setContrasena(passwordEncoder.encode(pass));
            nuevoUsuario.setRol(rol);
            // todo: se crearía acá el registro para Colaborador o Tecnico ??
            // TODO: Guardar nuevoUsuario en la base de datos usando repository
            // return repository.save(nuevoUsuario);
            return nuevoUsuario;
        } else return null;
    }
}
