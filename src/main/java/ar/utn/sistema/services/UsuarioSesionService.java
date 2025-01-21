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

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ColaboradorRepository rColaborador;
    @Autowired
    private TecnicoRepository rTecnico;
    @Autowired
    private AdminRepository rAdmin;


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
            switch (rol) {
                case "COLABORADOR_FISICO":
                    Colaborador rolClase = new ColaboradorFisico(nuevoUsuario);
                    rColaborador.save(rolClase);
                    break;
                case "COLABORADOR_JURIDICO":
                /*    Colaborador rolClase = new ColaboradorJuridico(nuevoUsuario);
                    rColaborador.save(rolClase);*/
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
            repository.save(nuevoUsuario);
            return nuevoUsuario;
        } else return null;
    }
}
