package ar.utn.sistema.config;

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
        if(sesion.obtenerUsuarioAutenticado().getNuevoUsuario() == 1)
            response.sendRedirect("/onboarding");
        else
            response.sendRedirect("/home");
    }
}
