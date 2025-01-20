package ar.utn.sistema.model;

import ar.utn.sistema.entities.usuarios.Rol;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// esta clase es para guardar datos del usuario que se logueó para que puedan ser consultados desde distintos lados del código
public class UsuarioSesionDetalle implements UserDetails {
    @Getter
    private String rol; // "COLABORADOR_FISICO", "COLABORADOR_JURIDICO", "TECNICO", "ADMIN/ONG"
    @Getter
    private Rol usuario;  // se puede guardar un Colaborador, Técnico o Admin
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UsuarioSesionDetalle(String rol, Rol usuario, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.rol = rol;
        this.usuario = usuario;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // se podría personalizar para deshabilitar la cuenta según la fecha de expiración en la db de ese usuario
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
