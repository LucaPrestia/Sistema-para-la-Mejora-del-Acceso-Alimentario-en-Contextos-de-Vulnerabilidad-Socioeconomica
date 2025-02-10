package ar.utn.sistema.config;

import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.model.UsuarioSesionDetalle;
import ar.utn.sistema.services.UsuarioSesionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerConfig implements AuthenticationSuccessHandler {
    @Autowired
    private UsuarioSesionService sesion;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UsuarioSesionDetalle usuarioAutenticado = sesion.obtenerUsuarioAutenticado();
        if(usuarioAutenticado.getNuevoUsuario() == 1)
            response.sendRedirect("/onboarding");
        else if (usuarioAutenticado.getNuevoUsuario() == 2) {
            response.sendRedirect("/cambioContrasenia");
        } else
            response.sendRedirect("/home");
    }
}
